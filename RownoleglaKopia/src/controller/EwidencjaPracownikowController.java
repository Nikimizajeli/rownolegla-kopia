package controller;

import model.*;
import view.Menu;
import view.TrybMenu;

import java.math.BigDecimal;
import java.util.*;

public class EwidencjaPracownikowController {
    private final Scanner scanner = new Scanner(System.in);
    private BazaPracownikow bazaPracownikow;
    private Menu menu;
    private TrybMenu trybMenu;

    public void start() {
        bazaPracownikow = new BazaPracownikow();
        menu = new Menu();
        trybMenu = TrybMenu.Korzen;
        int input;

        do {
            menu.wyswietlMenu(trybMenu);
            input = scanner.nextInt();
            scanner.nextLine();
            przetworzWyborTrybu(input);

        } while (input != 0);


        scanner.close();
    }

    private void przetworzWyborTrybu(int input) {
        switch (input) {
            case 1 -> {
                trybMenu = TrybMenu.ListaPracownikow;
                przetworzWyswietlanieListy();
                trybMenu = TrybMenu.Korzen;
            }
            case 2 -> {
                trybMenu = TrybMenu.DodajPracownika;
                przetworzDodawaniePracownika();
                trybMenu = TrybMenu.Korzen;
            }
            case 3 -> {
                trybMenu = TrybMenu.UsunPracownika;
                przetworzUsuwaniePracownika();
                trybMenu = TrybMenu.Korzen;
            }
            case 4 -> {
                trybMenu = TrybMenu.KopiaZapasowa;
                przetworzKopieZapasowa();
                trybMenu = TrybMenu.Korzen;
            }
            default -> trybMenu = TrybMenu.Korzen;
        }
    }

    private void przetworzWyswietlanieListy() {
        var pracownicy = bazaPracownikow.getPracownicy();
        int licznikPracownikow = 0;
        for (Pracownik pracownik : pracownicy.values()) {
            menu.wyswietlMenu(trybMenu);

            System.out.println(pracownik.toString());

            System.out.printf("%45s%n", String.format(Lokalizacja.MENU_POZYCJA, ++licznikPracownikow, pracownicy.size()));
            menu.wyswietlStopke(trybMenu, "Enter", "Q");
            String input = scanner.nextLine();
            while (!input.equalsIgnoreCase("Q")) {
                if (input.isEmpty()) {
                    break;
                }

                if (scanner.hasNextLine()) {
                    input = scanner.nextLine();
                } else {
                    input = "Q";
                }
            }
        }
    }

    private void przetworzDodawaniePracownika() {
        menu.wyswietlMenu(trybMenu);
        System.out.printf("%-30s ", Lokalizacja.DODAJ_PRACOWNIKA_WYBOR);
        Pracownik pracownik = pobierzDanePracownikaTest();                          // tutaj Test
        menu.wyswietlStopke(trybMenu, "Enter", "Q");
        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("Q")) {
            if (input.isEmpty()) {
                try {
                    bazaPracownikow.dodajPracownika(pracownik);
                } catch (IllegalArgumentException ex) {
                    System.err.println(ex.getMessage());
                }
                break;
            }

            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            } else {
                input = "Q";
            }
        }
    }

    private Pracownik pobierzDanePracownika() {
        Pracownik pracownik;
        boolean dyrektor = false;
        String input = scanner.nextLine();
        while (input != null) {
            if (input.equalsIgnoreCase("D")) {
                dyrektor = true;
                break;
            }
            if (input.equalsIgnoreCase("H")) {
                dyrektor = false;
                break;
            }
            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            } else {
                input = null;
            }
        }

        menu.wyswietlLinie();
        System.out.printf("%-30s:     ", Lokalizacja.PESEL);
        String pesel = scanner.nextLine();
        System.out.printf("%-30s:     ", Lokalizacja.IMIE);
        String imie = scanner.nextLine();
        System.out.printf("%-30s:     ", Lokalizacja.NAZWISKO);
        String nazwisko = scanner.nextLine();
        System.out.printf("%-30s:     ", Lokalizacja.WYNAGRODZENIE);
        BigDecimal wynagrodzenie = scanner.nextBigDecimal();
        scanner.nextLine();
        System.out.printf("%-30s:     ", Lokalizacja.NUMER_TEL);
        String nrTel = scanner.nextLine();

        if (dyrektor) {
            System.out.printf("%-30s:     ", Lokalizacja.DODATEK_SLUZBOWY);
            BigDecimal dodatek = scanner.nextBigDecimal();
            scanner.nextLine();
            System.out.printf("%-30s:     ", Lokalizacja.NUMER_KARTY);
            String nrKarty = scanner.nextLine();
            System.out.printf("%-30s:     ", Lokalizacja.LIMIT_KOSZTOW);
            BigDecimal limitKosztow = scanner.nextBigDecimal();
            scanner.nextLine();

            pracownik = new Dyrektor(pesel, imie, nazwisko, wynagrodzenie, nrTel, dodatek, nrKarty, limitKosztow);
        } else {
            System.out.printf("%-30s:     ", Lokalizacja.PROWIZJA);
            int procentProwizji = scanner.nextInt();
            scanner.nextLine();
            System.out.printf("%-30s:     ", Lokalizacja.LIMIT_PROWIZJI);
            BigDecimal limitProwizji = scanner.nextBigDecimal();
            scanner.nextLine();

            pracownik = new Handlowiec(pesel, imie, nazwisko, wynagrodzenie, nrTel, procentProwizji, limitProwizji);
        }

        menu.wyswietlLinie();
        return pracownik;
    }

    private Vector<String> pesele = new Vector<>(Arrays.asList("54491456446", "49240471375", "07921019696", "08222738480", "83090542865", "96282301410",
            "03111369736", "65911428589", "22250939226", "09231912108", "93092308749", "55880838999", "06421864188", "21812881283"));
    private List<String> imiona = Arrays.asList("Kamil", "Bogdan", "Andrzej", "Zdzisław", "Ryszard");
    private List<String> nazwiska = Arrays.asList("Rybak", "Kowal", "Stal", "Dworski", "Chleb");

    private Pracownik pobierzDanePracownikaTest(){
        Random rand = new Random();

        System.out.println();
        return new Dyrektor(pesele.remove(rand.nextInt(0, pesele.size())),
                imiona.get(rand.nextInt(0, imiona.size())),
                nazwiska.get(rand.nextInt(0, nazwiska.size())),
                BigDecimal.valueOf(rand.nextInt(100000)),
                Integer.toString(rand.nextInt(500000000,1000000000)),
                BigDecimal.valueOf(rand.nextInt(10000)),
                Integer.toString(rand.nextInt(10000)),
                BigDecimal.valueOf(rand.nextInt(1000, 1000000))
                );
    }

    private void przetworzUsuwaniePracownika() {
        menu.wyswietlMenu(trybMenu);
        System.out.printf("%-30s:     ", Lokalizacja.PODAJ_PESEL);
        String pesel = scanner.nextLine();
        Pracownik pracownik;
        try {
            pracownik = bazaPracownikow.pobierzPracownika(pesel);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        menu.wyswietlLinie();
        System.out.println(pracownik.toString());
        menu.wyswietlLinie();

        menu.wyswietlStopke(trybMenu, "Enter", "Q");

        String input = scanner.nextLine();
        while (!input.equalsIgnoreCase("Q")) {
            if (input.isEmpty()) {
                bazaPracownikow.usunPracownika(pesel);
                break;
            }

            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            } else {
                input = "Q";
            }
        }
    }

    private void przetworzKopieZapasowa() {
        menu.wyswietlMenu(trybMenu);
        System.out.printf("%-30s:     ", Lokalizacja.KOPIA_WYBOR);

        boolean odczyt = false;
        String input = scanner.nextLine();
        while (input != null) {
            if (input.equalsIgnoreCase("Z")) {
                odczyt = false;
                break;
            }
            if (input.equalsIgnoreCase("O")) {
                odczyt = true;
                break;
            }
            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            } else {
                input = null;
            }
        }

        menu.wyswietlLinie();
        boolean kompresjaGzip = true;
        if (!odczyt) {
            kompresjaGzip = wybierzKompresje();
        }

        System.out.printf("%-30s:     ", Lokalizacja.NAZWA_PLIKU);
        String nazwaPliku = scanner.nextLine();
        if (odczyt) {
            kompresjaGzip = wybierzKompresje(nazwaPliku);
        }
        menu.wyswietlLinie();

        menu.wyswietlStopke(trybMenu, "Enter", "Q");
        input = scanner.nextLine();
        while (!input.equalsIgnoreCase("Q")) {
            if (input.isEmpty()) {
                if (odczyt) {
                    bazaPracownikow.odtworzBazeZKopii(nazwaPliku, kompresjaGzip);
                } else {
                    bazaPracownikow.utworzKopiePracownikow(nazwaPliku, kompresjaGzip);
                }

                break;
            }

            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            } else {
                input = "Q";
            }
        }
    }

    private boolean wybierzKompresje() {
        return wybierzKompresje("");
    }

    private boolean wybierzKompresje(String nazwaPliku) {
        if (!nazwaPliku.isEmpty()) {
            if (nazwaPliku.endsWith(".gz")) {
                return true;
            }
            if (nazwaPliku.endsWith(".zip")) {
                return false;
            }
        }

        System.out.printf("%-30s:     ", Lokalizacja.KOMPRESJA);
        String input = scanner.nextLine();
        while (input != null) {
            if (input.equalsIgnoreCase("G")) {
                return true;
            }
            if (input.equalsIgnoreCase("Z")) {
                return false;
            }
            if (scanner.hasNextLine()) {
                input = scanner.nextLine();
            } else {
                input = null;
            }
        }

        return false;
    }
}
