package testy;

import czas.Interwal;
import czas.Moment;
import dziennik.Dziennik;
import dziennik.DziennikStandardoweWyjscie;
import kolejkaZdarzen.zdarzenia.DotarcieDoWezla;
import kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import losowosc.DeterministycznaMaszynaLosujaca;
import losowosc.MaszynaLosujaca;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import osrodek.Wezel;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.Sportowiec;
import sportowcy.SportowiecLokalny;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestyWyciagu {

    private static final Interwal ODSTEP_MIEDZY_ODJAZDAMI = new Interwal(10);
    private static final Interwal DLUGOSC_PRZEJAZDU = new Interwal(30);
    private static final Moment MOMENT0 = new Moment(9, 0, 0);
    private static final MaszynaLosujaca MASZYNA = new DeterministycznaMaszynaLosujaca(0);
    private Wyciag wyciag;
    private Wezel w0;
    private Wezel w1;
    private Sportowiec[] sportowcy;
    private Dziennik dziennik;

    @BeforeEach
    public void setup() {
        w0 = new Wezel(0, 100, 1, 1, true);
        w1 = new Wezel(1, 400, 10, 10, false);
        wyciag = new Wyciag(0, w0, w1, ODSTEP_MIEDZY_ODJAZDAMI, DLUGOSC_PRZEJAZDU, 3);
        dziennik = new DziennikStandardoweWyjscie();

        sportowcy = new Sportowiec[5];
        for (int i = 0; i < 5; i++) {
            sportowcy[i] = new SportowiecLokalny(i, 5, 0.3, 0.3, 0.3,
                    true, w0, MOMENT0, MASZYNA, 0.5, 0.4);
        }
    }

    private void dodajSportowcowDoKolejki(int indeksStart, int indeksKoniec) {
        for (int i = indeksStart; i < indeksKoniec; i++) {
            wyciag.dodajDoKolejki(sportowcy[i], MOMENT0);
        }
    }

    /**
     * Funkcja sprawdza, czy zbiór sportowców faktycznie zabranych jest zgodny z oczekiwanym.
     * Może okazać się, że implementacja przemiesza kolejność poprawnie wybranych sportowców, wtedy
     * zwykłe przyrównanie kolejnych elementów tablicy zwraca błędnie błąd w testach.
     */
    private void sprawdzPasazerow(Zdarzenie[] zdarzenia, List<Sportowiec> oczekiwani) {
        final List<Sportowiec> faktyczni = new ArrayList<>();

        for (int i = 1; i < zdarzenia.length; i++) {
            assertInstanceOf(DotarcieDoWezla.class, zdarzenia[i]);
            faktyczni.add(((DotarcieDoWezla) zdarzenia[i]).sportowiec());
        }

        assertEquals(oczekiwani.size(), faktyczni.size(), "Liczba faktycznych sportowcow nie zgadza się z oczekiwaną");
        assertTrue(faktyczni.containsAll(oczekiwani), "Brakuje niektórych oczekiwanych sportowców.");
        assertTrue(oczekiwani.containsAll(faktyczni), "W zdarzeniach są nieoczekiwani sportowcy");
    }

    @Test
    public void testPonadLimit3() {
        final int iluDodajemy = 4;
        assertEquals(0, wyciag.lacznaLiczbaPasazerow(), "Początkowo 0 przejazdów.");

        dodajSportowcowDoKolejki(0, iluDodajemy);
        final Zdarzenie[] zdarzenia = wyciag.odjazd(MOMENT0, dziennik);

        assertEquals(3 + 1, zdarzenia.length);
        assertInstanceOf(OdjazdWyciagu.class, zdarzenia[0]);

        sprawdzPasazerow(zdarzenia, List.of(sportowcy[0], sportowcy[1], sportowcy[2]));

        assertEquals(3, wyciag.lacznaLiczbaPasazerow());
    }

    @Test
    public void testPonizejLimitu3() {
        assertEquals(0, wyciag.lacznaLiczbaPasazerow(), "Początkowo 0 przejazdów.");

        dodajSportowcowDoKolejki(0, 2);
        final Zdarzenie[] zdarzenia = wyciag.odjazd(MOMENT0, dziennik);

        assertEquals(2 + 1, zdarzenia.length);
        assertInstanceOf(OdjazdWyciagu.class, zdarzenia[0]);

        sprawdzPasazerow(zdarzenia, List.of(sportowcy[0], sportowcy[1]));

        assertEquals(2, wyciag.lacznaLiczbaPasazerow());
    }

    @Test
    public void testMaksDlugosciKolejki() {
        assertEquals(0, wyciag.lacznaLiczbaPasazerow(), "Początkowo 0 przejazdów.");

        dodajSportowcowDoKolejki(0, 4);
        assertEquals(4, wyciag.maksDlugoscKolejki(), "4 sportowców w kolejce.");

        final Zdarzenie[] zdarzenia = wyciag.odjazd(MOMENT0, dziennik);

        assertEquals(3 + 1, zdarzenia.length);
        assertEquals(4, wyciag.maksDlugoscKolejki(), "Długość nie powinna się zmienić.");

        wyciag.dodajDoKolejki(sportowcy[4], MOMENT0.dodajInterwal(new Interwal(2)));

        assertEquals(4, wyciag.maksDlugoscKolejki(), "Długość nie powinna się zmienić.");
    }

}
