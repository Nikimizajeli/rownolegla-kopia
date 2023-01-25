package model;

import java.util.HashMap;
import java.util.Map;

public class BazaPracownikow {
    private final Map<String, Pracownik> pracownicy;
    private KopiaZapasowa kopiaZapasowa;

    public BazaPracownikow() {
        pracownicy = new HashMap<String, Pracownik>();
    }

    public void dodajPracownika(Pracownik pracownik) throws IllegalArgumentException {
        if (!czyPeselJestPoprawny(pracownik.getPesel())) {
            throw new IllegalArgumentException("Niepoprawny numer pesel");
        }
        pracownicy.put(pracownik.getPesel(), pracownik);
    }

    public Pracownik pobierzPracownika(String pesel) throws IllegalArgumentException {
        Pracownik pracownik = pracownicy.get(pesel);

        if(pracownik == null){
            throw new IllegalArgumentException("W bazie nie ma pracownika o takim numerze pesel.");
        }

        return pracownik;
    }

    public void usunPracownika(String pesel){
        pracownicy.remove(pesel);
    }

    private boolean czyPeselJestPoprawny(String pesel) {
        if (pracownicy.containsKey(pesel)) {
            return false;
        }

        if (pesel.length() != 11) {
            return false;
        }

        int p[] = new int[11];
        for (int i = 0; i < 11; i++) {
            p[i] = Integer.parseInt(pesel.substring(i, i + 1));
        }

        return (p[0] + 3 * p[1] + 7 * p[2] + 9 * p[3] + p[4] + 3 * p[5] + 7 * p[6] + 9 * p[7] + p[8] + 3 * p[9] + p[10]) % 10 == 0;
    }

    public void utworzKopiePracownikow(String nazwaPliku, boolean kompresjaGzip){
        if(kopiaZapasowa == null){
            kopiaZapasowa = new KopiaZapasowa(this);
        }

        kopiaZapasowa.utworzKopiePracownikow(nazwaPliku, kompresjaGzip);
    }

    public void odtworzBazeZKopii(String nazwaPliku, boolean kompresjaGzip){
        if(kopiaZapasowa == null){
            kopiaZapasowa = new KopiaZapasowa(this);
        }

        var odtworzeniPracownicy = kopiaZapasowa.odwtorzBazePracownikow(nazwaPliku, kompresjaGzip);
        pracownicy.putAll(odtworzeniPracownicy);
    }

    public Map<String, Pracownik> getPracownicy() {
        return pracownicy;
    }
}
