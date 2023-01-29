package model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.zip.*;

public class KopiaZapasowa {
    private BazaPracownikow baza;

    public KopiaZapasowa(BazaPracownikow bazaPracownikow) {
        this.baza = bazaPracownikow;
    }

    public void utworzKopiePracownikow(String nazwaPliku, boolean kompresjaGzip) {
        var executorService = Executors.newFixedThreadPool(10);
        ArrayList<CompletableFuture<Void>> results = new ArrayList<>();

        for (Pracownik pracownik :
                baza.getPracownicy().values()) {
            results.add(CompletableFuture.runAsync(new ZapisPracownika(pracownik, nazwaPliku, kompresjaGzip), executorService));
        }

        for (var result :
                results) {
            try {
                result.get();
            } catch (ExecutionException e) {
                System.err.println("Cos poszlo nie tak" + e.getCause());
            } catch (InterruptedException e) {
                System.err.println("Watek zostal przerwany");
            }
        }

        executorService.shutdown();

    }

    public HashMap<String, Pracownik> odwtorzBazePracownikow(String nazwaPliku, boolean kompresjaGzip) {
        HashMap<String, Pracownik> nowaMapa;
        try (FileInputStream fis = new FileInputStream(nazwaPliku)) {
            Object obj;
            if (kompresjaGzip) {
                obj = odtworzBazePracownikowGzip(fis);
            } else {
                obj = odtworzBazePracownikowZip(fis);
            }

            if (!(obj instanceof HashMap<?, ?> mapa)) {
                throw new RuntimeException("Kopia nie jest mapa.");
            }

            if (mapa.size() < 1) {
                throw new RuntimeException("Kopia jest pusta.");
            }

            for (Object o : mapa.values()) {
                if (!(o instanceof Pracownik)) {
                    throw new RuntimeException("Zly plik lub kopia jest uszkodzona.");
                }
            }
            nowaMapa = (HashMap<String, Pracownik>) mapa;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return nowaMapa;
    }

    private Object odtworzBazePracownikowGzip(FileInputStream fis) {
        try (GZIPInputStream gis = new GZIPInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(gis)) {

            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Object odtworzBazePracownikowZip(FileInputStream fis) {
        try {
            ZipInputStream zis = new ZipInputStream(fis);
            zis.getNextEntry();
            ObjectInputStream ois = new ObjectInputStream(zis);

            Object obj = ois.readObject();

            ois.close();
            zis.close();

            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

class ZapisPracownika implements Runnable {

    private final Pracownik pracownik;
    private String nazwaPliku;
    private boolean kompresjaGzip;

    public ZapisPracownika(Pracownik pracownik, String nazwaPliku, boolean kompresjaGzip) {
        this.pracownik = pracownik;
        this.nazwaPliku = nazwaPliku;
        this.kompresjaGzip = kompresjaGzip;
    }

    @Override
    public void run() {
        if (nazwaPliku.endsWith(".zip") || nazwaPliku.endsWith(".gz")) {
            nazwaPliku = nazwaPliku.replaceAll("(?<!^)[.][^.]*$", "");
        }
        String rozszerzenie = kompresjaGzip ? ".gz" : ".zip";
        nazwaPliku = String.format("%s_%s%s", nazwaPliku, pracownik.getPesel(), rozszerzenie);

        if (kompresjaGzip) {
            zrobGzipa();
        } else {
            zrobZipa();
        }

    }

    private void zrobGzipa() {
        try (ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(nazwaPliku))))) {
            out.writeObject(pracownik);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void zrobZipa() {
        try (FileOutputStream fos = new FileOutputStream(nazwaPliku);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ZipOutputStream zos = new ZipOutputStream(bos)) {
            zos.putNextEntry(new ZipEntry(pracownik.getPesel()));
            try (ObjectOutputStream out = new ObjectOutputStream(zos)) {
                out.writeObject(pracownik);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
