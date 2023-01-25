package view;

import model.Lokalizacja;

public class Menu {

    public void wyswietlMenu(TrybMenu tryb) {
        switch (tryb) {
            case Korzen -> {
                System.out.println(Lokalizacja.MENU_NAGLOWEK);
                System.out.printf("\t1. %s%n", Lokalizacja.MENU_LISTA);
                System.out.printf("\t2. %s%n", Lokalizacja.MENU_DODAJ);
                System.out.printf("\t3. %s%n", Lokalizacja.MENU_USUN);
                System.out.printf("\t4. %s%n", Lokalizacja.MENU_KOPIA);
                System.out.println(Lokalizacja.MENU_WYBOR);
            }
            case ListaPracownikow -> System.out.printf("1. %s%n%n", Lokalizacja.MENU_LISTA);
            case DodajPracownika -> System.out.printf("2. %s%n%n", Lokalizacja.MENU_DODAJ);
            case UsunPracownika -> System.out.printf("3. %s%n%n", Lokalizacja.MENU_USUN);
            case KopiaZapasowa -> System.out.printf("4. %s%n%n", Lokalizacja.MENU_KOPIA);
            default -> System.err.println("Nie ma takiego trybu menu.");
        }
    }

    public void wyswietlStopke(TrybMenu tryb, String przyciskDalej, String przyciskWstecz) {
        switch (tryb) {
            case ListaPracownikow -> {
                System.out.printf(Lokalizacja.NASTEPNY + "%n", przyciskDalej);
                System.out.printf(Lokalizacja.POWROT + "%n", przyciskWstecz);
            }
            case DodajPracownika -> {
                System.out.printf(Lokalizacja.ZAPISZ + "%n", przyciskDalej);
                System.out.printf(Lokalizacja.POWROT + "%n", przyciskWstecz);
            }
            case UsunPracownika, KopiaZapasowa -> {
                System.out.printf(Lokalizacja.POTWIERDZ + "%n", przyciskDalej);
                System.out.printf(Lokalizacja.POWROT + "%n", przyciskWstecz);
            }
            default -> System.err.println("Nie ma takiego trybu menu.");
        }
    }

    public void wyswietlLinie() {
        System.out.println("-".repeat(40));
    }

}
