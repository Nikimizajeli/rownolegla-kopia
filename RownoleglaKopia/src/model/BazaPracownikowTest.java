package model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BazaPracownikowTest {
    final static String[] POPRAWNE_PESELE = {"54491456446", "49240471375", "07921019696", "08222738480", "83090542865", "96282301410",
            "03111369736", "65911428589", "22250939226", "09231912108", "93092308749", "55880838999", "06421864188", "21812881283"};

    final static String[] NIEPOPRAWNE_PESELE = {"11111111111", "33333333333", "55555555555", "77777777777", "99999999999", "1234",
            "0987654", "tonawetnieliczba", "1234567890987654321", "151515151515", "09138805423", "22222209876"};
    private BazaPracownikow baza;
    private Random rand;

    private Vector<String> poprawnePesele;
    private List<String> imiona;
    private List<String> nazwiska;

    @org.junit.jupiter.api.BeforeEach
    void init() {
        baza = new BazaPracownikow();
        rand = new Random();
        poprawnePesele = new Vector<>(Arrays.asList(POPRAWNE_PESELE));

        imiona = Arrays.asList("Kamil", "Bogdan", "Andrzej", "Zdzisław", "Ryszard", "Leszek", "Baltazar");
        nazwiska = Arrays.asList("Rybak", "Kowal", "Stal", "Dworski", "Chleb", "Chmiel", "Winny", "Albo", "Tron");
    }

    // 1.1) dodanie pracownika typu Handlowiec do pustego kontenera
    @org.junit.jupiter.api.Test
    void dodajPracownika_HandlowiecPustaBaza() {
        Handlowiec handlowiec = generujHandlowca();

        baza.dodajPracownika(handlowiec);
        assertEquals(handlowiec, baza.pobierzPracownika(handlowiec.getPesel()));
    }

    // 1.2) dodanie pracownika typu Dyrektor do pustego kontenera
    @org.junit.jupiter.api.Test
    void dodajPracownika_DyrektorPustaBaza() {
        Dyrektor dyrektor = generujDyrektora();

        baza.dodajPracownika(dyrektor);
        assertEquals(dyrektor, baza.pobierzPracownika(dyrektor.getPesel()));
    }

    // 1.3) dodanie pracownika typu Handlowiec do kontenera zawierającego innych pracowników
    @org.junit.jupiter.api.Test
    void dodajPracownika_Handlowiec() {
        Dyrektor dyrektor = generujDyrektora();
        baza.dodajPracownika(dyrektor);
        Handlowiec handlowiec = generujHandlowca();
        baza.dodajPracownika(handlowiec);

        Handlowiec nowyHandlowiec = generujHandlowca();
        baza.dodajPracownika(nowyHandlowiec);
        assertEquals(nowyHandlowiec, baza.pobierzPracownika(nowyHandlowiec.getPesel()));
    }

    // 1.4) dodanie pracownika typu Dyrektor do kontenera zawierającego innych pracowników
    @org.junit.jupiter.api.Test
    void dodajPracownika_Dyrektor() {
        Dyrektor dyrektor = generujDyrektora();
        baza.dodajPracownika(dyrektor);
        Handlowiec handlowiec = generujHandlowca();
        baza.dodajPracownika(handlowiec);

        Dyrektor nowyDyrektor = generujDyrektora();
        baza.dodajPracownika(nowyDyrektor);
        assertEquals(nowyDyrektor, baza.pobierzPracownika(nowyDyrektor.getPesel()));
    }

    // 1.5) dodanie do pustego kontenera minimum 10 pracowników losowych typów
    @org.junit.jupiter.api.Test
    void dodajPracownika_DziesieciuLosowych() {
        for (int i = 0; i < 12; i++) {
            Pracownik pracownik = (rand.nextInt(0, 2) & 1) == 1 ? generujDyrektora() : generujHandlowca();
            baza.dodajPracownika(pracownik);
        }

        assertEquals(12, baza.getPracownicy().size());
    }

    // 1.6) usunięcie pracownika typu Handlowiec z kontenera zawierającego innych pracowników
    @org.junit.jupiter.api.Test
    void usunPracownika_Handlowiec() {
        baza.dodajPracownika(generujHandlowca());
        baza.dodajPracownika(generujDyrektora());

        Handlowiec handlowiec = generujHandlowca();
        baza.dodajPracownika(handlowiec);
        baza.usunPracownika(handlowiec.getPesel());

        assertThrows(IllegalArgumentException.class, () -> {
            baza.pobierzPracownika(handlowiec.getPesel());
        });
    }

    // 1.7) usunięcie pracownika typu Dyrektor z kontenera zawierającego innych pracowników
    @org.junit.jupiter.api.Test
    void usunPracownika_Dyrektor() {
        baza.dodajPracownika(generujHandlowca());
        baza.dodajPracownika(generujDyrektora());

        Dyrektor dyrektor = generujDyrektora();
        baza.dodajPracownika(dyrektor);
        baza.usunPracownika(dyrektor.getPesel());

        assertThrows(IllegalArgumentException.class, () -> {
            baza.pobierzPracownika(dyrektor.getPesel());
        });
    }

    // 1.8) testy sparametryzowane (parameterized test) dla weryfikacji poprawności sumy kontrolnej numeru PESEL
    static Stream<String> dostawcaPoprawnychPESELi() {
        return Stream.of(POPRAWNE_PESELE);
    }

    @ParameterizedTest
    @MethodSource("dostawcaPoprawnychPESELi")
    void czyPeselJestPoprawny_Tak(String pesel) {
        assertTrue(baza.czyPeselJestPoprawny(pesel));
    }

    static Stream<String> dostawcaNiepoprawnychPESELi() {
        return Stream.of(NIEPOPRAWNE_PESELE);
    }

    @ParameterizedTest
    @MethodSource("dostawcaNiepoprawnychPESELi")
    void czyPeselJestPoprawny_Nie(String pesel) {
        assertFalse(baza.czyPeselJestPoprawny(pesel));
    }

    @org.junit.jupiter.api.Test
    void utworzKopiePracownikow_Gzip() {
        String nazwaPliku = "testZapisu";
        Pracownik[] pracownicy = {generujDyrektora(), generujHandlowca(), generujHandlowca(), generujDyrektora()};
        for (Pracownik pracownik:
             pracownicy) {
            baza.dodajPracownika(pracownik);
        }
        baza.utworzKopiePracownikow(nazwaPliku, true);

        File[] pliki = new File[4];
        for(int i = 0; i < 4; i++){
            pliki[i] = new File(nazwaPliku + '_' + pracownicy[i].getPesel() + ".gz");
        }

        boolean plikiUtworzonePoprawnie = true;
        for (File plik:
             pliki) {
            plikiUtworzonePoprawnie &= plik.exists();
        }

        assertTrue(plikiUtworzonePoprawnie);
    }

    @org.junit.jupiter.api.Test
    void odtworzBazeZKopii() {
        fail("Ten test jest jeszcze pusty.");
    }

    @org.junit.jupiter.api.Test
    void getPracownicy() {
        fail("Ten test jest jeszcze pusty.");
    }

    private Handlowiec generujHandlowca() {
        return new Handlowiec(poprawnePesele.remove(rand.nextInt(0, poprawnePesele.size())),
                imiona.get(rand.nextInt(0, imiona.size())),
                nazwiska.get(rand.nextInt(0, nazwiska.size())),
                BigDecimal.valueOf(rand.nextInt(100000)),
                Integer.toString(rand.nextInt(500000000, 1000000000)),
                rand.nextInt(20),
                BigDecimal.valueOf(rand.nextInt(100000))
        );
    }

    private Dyrektor generujDyrektora() {
        return new Dyrektor(poprawnePesele.remove(rand.nextInt(0, poprawnePesele.size())),
                imiona.get(rand.nextInt(0, imiona.size())),
                nazwiska.get(rand.nextInt(0, nazwiska.size())),
                BigDecimal.valueOf(rand.nextInt(100000)),
                Integer.toString(rand.nextInt(500000000, 1000000000)),
                BigDecimal.valueOf(rand.nextInt(10000)),
                Integer.toString(rand.nextInt(10000)),
                BigDecimal.valueOf(rand.nextInt(1000, 1000000))
        );
    }
}