package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Pracownik implements Serializable {
    private String pesel;
    private String imie;
    private String nazwisko;
    private String stanowisko;
    private BigDecimal wynagrodzenie;
    private String numerTelefonu;

    public Pracownik(String pesel, String imie, String nazwisko, String stanowisko, BigDecimal wynagrodzenie, String numerTelefonu) {
        this.pesel = pesel;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.stanowisko = stanowisko;
        this.wynagrodzenie = wynagrodzenie;
        this.numerTelefonu = numerTelefonu;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getStanowisko() {
        return stanowisko;
    }

    public void setStanowisko(String stanowisko) {
        this.stanowisko = stanowisko;
    }

    public BigDecimal getWynagrodzenie() {
        return wynagrodzenie;
    }

    public void setWynagrodzenie(BigDecimal wynagrodzenie) {
        this.wynagrodzenie = wynagrodzenie;
    }

    public String getNumerTelefonu() {
        return numerTelefonu;
    }

    public void setNumerTelefonu(String numerTelefonu) {
        this.numerTelefonu = numerTelefonu;
    }

    @Override
    public String toString(){
        return String.format(
                "%-30s:     %s%n" +
                "%-30s:     %s%n" +
                "%-30s:     %s%n" +
                "%-30s:     %s%n" +
                "%-30s:     %s%n" +
                "%-30s:     %s%n",
                Lokalizacja.PESEL, getPesel(),
                Lokalizacja.IMIE, getImie(),
                Lokalizacja.NAZWISKO, getNazwisko(),
                Lokalizacja.STANOWISKO, getStanowisko(),
                Lokalizacja.WYNAGRODZENIE, getWynagrodzenie(),
                Lokalizacja.NUMER_TEL, getNumerTelefonu()
        );
    }
}
