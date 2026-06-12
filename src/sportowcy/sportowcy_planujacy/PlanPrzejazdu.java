package sportowcy.sportowcy_planujacy;

import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;

import java.util.ArrayDeque;
import java.util.Queue;

public class PlanPrzejazdu {

    private final SportowiecPlanujacy sportowiec;
    private Queue<Krawedz> plan;

    public PlanPrzejazdu(SportowiecPlanujacy sportowiec) {
        plan = new ArrayDeque<>();
        this.sportowiec = sportowiec;
    }

    public boolean czyPusta() {
        return plan.isEmpty();
    }

    /**
     * Zwraca zdarzenie następnego kroku dla pierwszego wydarzenie z kolejki
     * planu przejazdu.
     */
    public Zdarzenie pobierzZdarzenieNastepnegoKroku(Moment moment) {
        assert !czyPusta() : "Próba pobrania z pustego planu";

        final Krawedz nastepna = plan.poll();
        assert nastepna != null : "Krawędź w kolejce jest nullem";

        return nastepna.zdarzenieNastepnegoKroku(moment, sportowiec);
    }

    /**
     * Układa nowy plan przejazdu dla SportowcaPlanującego dla
     * porządanej przez niego trasy.
     */
    public void przygotuj(Wezel obecny, Osrodek osrodek) {
        final PrzeszukiwanieGrafu bfs = new PrzeszukiwanieBfs(obecny);
        final Trasa wymarzonaTrasa = sportowiec.znajdzWymarzonaTrase(osrodek, bfs);

        if (wymarzonaTrasa != null) {
            // Wyznaczamy sciezke do początku trasy.
            plan = bfs.wyznaczSciezke(wymarzonaTrasa.poczatek());

            if (plan != null) {
                // Do sciezki do początku trasy dodajemy zjazd wymarzoną trasą.
                plan.add(wymarzonaTrasa);
            }
        }

    }

}
