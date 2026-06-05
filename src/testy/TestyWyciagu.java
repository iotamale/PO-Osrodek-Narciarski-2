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
                    true, w0, MOMENT0, MASZYNA, 0.5, 0.2);
        }
    }

    private void dodajSportowcowDoKolejki(int indeksStart, int indeksKoniec) {
        for (int i = indeksStart; i < indeksKoniec; i++) {
            wyciag.dodajDoKolejki(sportowcy[i], MOMENT0);
        }
    }

    @Test
    public void testPonadLimit3i() {
        final int iluDodajemy = 4;
        assertEquals(0, wyciag.lacznaLiczbaPasazerow());

        dodajSportowcowDoKolejki(0, iluDodajemy);
        final Zdarzenie[] zdarzenia = wyciag.odjazd(MOMENT0, dziennik);

        assertEquals(3 + 1, zdarzenia.length);
        assertInstanceOf(OdjazdWyciagu.class, zdarzenia[0]);

        for (int i = 0; i < 3; i++) {
            assertInstanceOf(DotarcieDoWezla.class, zdarzenia[1 + i]);
            assertEquals(sportowcy[i], ((DotarcieDoWezla) zdarzenia[1 + i]).sportowiec());
        }

        assertEquals(3, wyciag.lacznaLiczbaPasazerow());
    }

    @Test
    public void testPonizejLimitu3i() {
        assertEquals(0, wyciag.lacznaLiczbaPasazerow());

        dodajSportowcowDoKolejki(0, 2);
        final Zdarzenie[] zdarzenia = wyciag.odjazd(MOMENT0, dziennik);

        assertEquals(2 + 1, zdarzenia.length);
        assertInstanceOf(OdjazdWyciagu.class, zdarzenia[0]);

        for (int i = 0; i < 2; i++) {
            assertInstanceOf(DotarcieDoWezla.class, zdarzenia[1 + i]);
            assertEquals(sportowcy[i], ((DotarcieDoWezla) zdarzenia[1 + i]).sportowiec());
        }

        assertEquals(2, wyciag.lacznaLiczbaPasazerow());
    }

    @Test
    public void testMaksDlugosciKolejki() {
        assertEquals(0, wyciag.maksDlugoscKolejki());

        dodajSportowcowDoKolejki(0, 4);
        assertEquals(4, wyciag.maksDlugoscKolejki());

        final Zdarzenie[] zdarzenia = wyciag.odjazd(MOMENT0, dziennik);

        assertEquals(3 + 1, zdarzenia.length);
        assertEquals(4, wyciag.maksDlugoscKolejki());

        wyciag.dodajDoKolejki(sportowcy[4], MOMENT0.dodajInterwal(new Interwal(2)));

        assertEquals(4, wyciag.maksDlugoscKolejki());
    }

}
