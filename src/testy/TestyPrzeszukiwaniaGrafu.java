package testy;

import czas.Interwal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.sportowcy_planujacy.PrzeszukiwanieBfs;
import sportowcy.sportowcy_planujacy.PrzeszukiwanieGrafu;

import java.util.Queue;
import static org.junit.jupiter.api.Assertions.*;

public class TestyPrzeszukiwaniaGrafu {

    private static final Interwal INTERWAL = new Interwal(10);
    private static final int[] WYSOKOSCI = {0, 10, 5, 20, 15, 30}; // Konstruktory Krawedzi sprawdzaja poprawnosc.

    private Wezel[] w;

    @BeforeEach
    public void przygotujGraf() {
        w = new Wezel[6];

        for (int i = 0; i < 6; i++) {
            w[i] = new Wezel(i, WYSOKOSCI[i], 0, 0, false);
        }

        w[0].wychodzaceWyciagi(new Wyciag[]{
                new Wyciag(0, w[0], w[1], INTERWAL, INTERWAL, 10)
        });
        w[0].wychodzaceTrasy(new Trasa[]{});

        w[1].wychodzaceWyciagi(new Wyciag[]{});
        w[1].wychodzaceTrasy(new Trasa[]{
                new Trasa(4, w[1], w[0], INTERWAL, 10, 1.0, 1.0),
                new Trasa(5, w[1], w[2], INTERWAL, 10, 1.0, 1.0)
        });

        w[2].wychodzaceWyciagi(new Wyciag[]{
                new Wyciag(1, w[2], w[4], INTERWAL, INTERWAL, 10),
                new Wyciag(2, w[2], w[3], INTERWAL, INTERWAL, 10)
        });
        w[2].wychodzaceTrasy(new Trasa[]{
                new Trasa(6, w[2], w[0], INTERWAL, 10, 1.0, 1.0)
        });

        w[3].wychodzaceWyciagi(new Wyciag[]{});
        w[3].wychodzaceTrasy(new Trasa[]{
                new Trasa(7, w[3], w[1], INTERWAL, 10, 1.0, 1.0),
                new Trasa(8, w[3], w[4], INTERWAL, 10, 1.0, 1.0),
        });

        w[4].wychodzaceWyciagi(new Wyciag[]{
                new Wyciag(3, w[4], w[5], INTERWAL, INTERWAL, 10)
        });
        w[4].wychodzaceTrasy(new Trasa[]{});

        w[5].wychodzaceWyciagi(new Wyciag[]{});
        w[5].wychodzaceTrasy(new Trasa[]{
                new Trasa(10, w[5], w[3], INTERWAL, 10, 1.0, 1.0),
                new Trasa(11, w[5], w[3], INTERWAL, 10, 1.0, 1.0)
        });
    }

    @Test
    public void testSciezka0Do4() {
        final PrzeszukiwanieGrafu bfs = new PrzeszukiwanieBfs(w[0]);

        assertEquals(3, bfs.pobierzOdleglosc(w[4]));

        final Queue<Krawedz> sciezka = bfs.wyznaczSciezke(w[4]);
        assertNotNull(sciezka);
        assertEquals(3, sciezka.size());

        final Krawedz[] kroki = sciezka.toArray(new Krawedz[0]);
        assertEquals(w[1], kroki[0].koniec(), "Krok 1. 0->1");
        assertEquals(w[2], kroki[1].koniec(), "Krok 2. 1->2");
        assertEquals(w[4], kroki[2].koniec(), "Krok 3. 2->4");
    }

    @Test
    void testBezposredniaSciezka3Do1() {
        final PrzeszukiwanieGrafu bfs = new PrzeszukiwanieBfs(w[3]);

        assertEquals(1, bfs.pobierzOdleglosc(w[1]));

        final Queue<Krawedz> sciezka = bfs.wyznaczSciezke(w[1]);
        assertNotNull(sciezka);
        assertEquals(1, sciezka.size());

        assertNotNull(sciezka.peek());
        assertEquals(w[1], sciezka.peek().koniec());
    }

    @Test
    void testPustaSciezka2DoSamegoSiebie() {
        final PrzeszukiwanieGrafu bfs = new PrzeszukiwanieBfs(w[2]);

        assertEquals(0, bfs.pobierzOdleglosc(w[2]));

        final Queue<Krawedz> sciezka = bfs.wyznaczSciezke(w[2]);
        assertNotNull(sciezka);
        assertTrue(sciezka.isEmpty(), "Sciezka do samego siebie powinna być pusta.");
    }

    @Test
    void testPodwojnyWybor4Do3() {
        final PrzeszukiwanieGrafu bfs = new PrzeszukiwanieBfs(w[4]);

        assertEquals(2, bfs.pobierzOdleglosc(w[3]));

        final Queue<Krawedz> sciezka = bfs.wyznaczSciezke(w[3]);
        assertNotNull(sciezka);
        assertEquals(2, sciezka.size());

        final Krawedz[] kroki = sciezka.toArray(new Krawedz[0]);
        assertEquals(w[5], kroki[0].koniec(), "Krok 1. 4->5");
        assertEquals(w[3], kroki[1].koniec(), "Krok 2. 5->3");
    }

}
