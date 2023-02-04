package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Dyrektor extends Pracownik {
    private BigDecimal dodatekSluzbowy;
    private String numerKarty;
    private BigDecimal limitKosztow;

    public Dyrektor(String pesel, String imie, String nazwisko,BigDecimal wynagrodzenie, String numerTelefonu, BigDecimal dodatekSluzbowy, String numerKarty, BigDecimal limitKosztow) {
        super(pesel, imie, nazwisko, Dyrektor.class.getSimpleName(), wynagrodzenie, numerTelefonu);
        this.dodatekSluzbowy = dodatekSluzbowy;
        this.numerKarty = numerKarty;
        this.limitKosztow = limitKosztow;
    }

    public BigDecimal getDodatekSluzbowy() {
        return dodatekSluzbowy;
    }

    public void setDodatekSluzbowy(BigDecimal dodatekSluzbowy) {
        this.dodatekSluzbowy = dodatekSluzbowy;
    }

    public String getNumerKarty() {
        return numerKarty;
    }

    public void setNumerKarty(String numerKarty) {
        this.numerKarty = numerKarty;
    }

    public BigDecimal getLimitKosztow() {
        return limitKosztow;
    }

    public void setLimitKosztow(BigDecimal limitKosztow) {
        this.limitKosztow = limitKosztow;
    }

    @Override
    public String toString(){
        return String.format(
                "%s" +
                "%-30s:     %s%n" +
                "%-30s:     %s%n" +
                "%-30s:     %s%n",
                super.toString(),
                Lokalizacja.DODATEK_SLUZBOWY, getDodatekSluzbowy().toString(),
                Lokalizacja.NUMER_KARTY, getNumerKarty(),
                Lokalizacja.LIMIT_KOSZTOW, getLimitKosztow().toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Dyrektor dyrektor = (Dyrektor) o;
        return Objects.equals(dodatekSluzbowy, dyrektor.dodatekSluzbowy) && Objects.equals(numerKarty, dyrektor.numerKarty) && Objects.equals(limitKosztow, dyrektor.limitKosztow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dodatekSluzbowy, numerKarty, limitKosztow);
    }
}
