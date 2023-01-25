package model;

import java.io.*;
import java.util.HashMap;
import java.util.zip.*;

public class KopiaZapasowa {
    private BazaPracownikow baza;

    public KopiaZapasowa(BazaPracownikow bazaPracownikow) {
        this.baza = bazaPracownikow;
    }

    public void utworzKopiePracownikow(String nazwaPliku, boolean kompresjaGzip) {
        if (kompresjaGzip) {
            utworzKopiePracownikowGzip(nazwaPliku);
        } else {
            utworzKopiePracownikowZip(nazwaPliku);
        }
    }

    private void utworzKopiePracownikowGzip(String nazwaPliku) {
        try (ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(nazwaPliku))))) {
            out.writeObject(baza.getPracownicy());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void utworzKopiePracownikowZip(String nazwaPliku) {
        try (FileOutputStream fos = new FileOutputStream(nazwaPliku);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ZipOutputStream zos = new ZipOutputStream(bos)) {
            zos.putNextEntry(new ZipEntry("Baza pracownikow"));
            ObjectOutputStream out = new ObjectOutputStream(zos);
            out.writeObject(baza.getPracownicy());
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
