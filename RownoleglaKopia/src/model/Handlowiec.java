package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Handlowiec extends Pracownik {
    private int procentProwizji;
    private BigDecimal limitProwizji;

    public Handlowiec(String pesel, String imie, String nazwisko, BigDecimal wynagrodzenie, String numerTelefonu, int procentProwizji, BigDecimal limitProwizji) {
        super(pesel, imie, nazwisko, Handlowiec.class.getSimpleName(), wynagrodzenie, numerTelefonu);
        this.procentProwizji = procentProwizji;
        this.limitProwizji = limitProwizji;
    }

    public int getProcentProwizji() {
        return procentProwizji;
    }

    public void setProcentProwizji(int procentProwizji) {
        this.procentProwizji = procentProwizji;
    }

    public BigDecimal getLimitProwizji() {
        return limitProwizji;
    }

    public void setLimitProwizji(BigDecimal limitProwizji) {
        this.limitProwizji = limitProwizji;
    }

    @Override
    public String toString(){
        return String.format(
                "%s" +
                "%-30s:     %d%n" +
                "%-30s:     %s%n",
                super.toString(),
                Lokalizacja.PROWIZJA, getProcentProwizji(),
                Lokalizacja.LIMIT_PROWIZJI, getLimitProwizji().toString()

        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Handlowiec that = (Handlowiec) o;
        return procentProwizji == that.procentProwizji && Objects.equals(limitProwizji, that.limitProwizji);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), procentProwizji, limitProwizji);
    }
}
