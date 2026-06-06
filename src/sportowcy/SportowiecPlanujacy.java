package sportowcy;

import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import losowosc.MaszynaLosujaca;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;

import java.util.Queue;

public abstract class SportowiecPlanujacy extends Sportowiec {

    private Queue<Krawedz> planPrzejazdu;

    public SportowiecPlanujacy(int id,
                             int poziomZaawansowania,
                             double wspolczynnikSpontanicznosci,
                             double wagaTrudnosci,
                             double wagaNawierzchni,
                             boolean sledzony,
                             Wezel wezelStartowy,
                             Moment momentStartu,
                             MaszynaLosujaca maszynaLosujaca,
                             double wspolczynnikZnudzenia, double wagaZnudzenia) {
        planPrzejazdu = null;
        super(id, poziomZaawansowania, wspolczynnikSpontanicznosci, wagaTrudnosci, wagaNawierzchni, sledzony, wezelStartowy,
                momentStartu, maszynaLosujaca, wspolczynnikZnudzenia, wagaZnudzenia);
    }

    /**
     * Funkcja odpowiedzialna za wyznaczenie najlepszej trasy dla planującego sportowca.
     */
    protected abstract Trasa znajdzWymarzonaTrase(Osrodek osrodek, PrzeszukiwanieGrafu bfs);

    /**
     * Zwraca zdarzenie adekwatne do następnej pozycji w planie.
     */
    private Zdarzenie pobierzNastepnyZPlanu(Moment moment) {
        assert planPrzejazdu != null && !planPrzejazdu.isEmpty() : "Próba pobrania z pustej kolejki planu";

        return planPrzejazdu.poll().zdarzenieNastepnegoKroku(moment, this);
    }

    @Override
    public Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel, Osrodek osrodek) {
        // Zmiana planu i spontanicznosc sa dostepne tylko, gdy poprzedni plan zostal zrealizowany.
        if (planPrzejazdu != null && !planPrzejazdu.isEmpty()) {
            return pobierzNastepnyZPlanu(moment);
        }

        // Nie mamy planu to mozemy zachowac sie spontanicznie.
        if (czyNastepnyKrokLosowy()) {
            return podejmijSpontanicznaDecyzje(moment, obecnyWezel);
        }

        // BFS
        final PrzeszukiwanieGrafu bfs = new PrzeszukiwanieGrafu(obecnyWezel);
        final Trasa wymarzonaTrasa = znajdzWymarzonaTrase(osrodek, bfs);

        if (wymarzonaTrasa != null) {
            // Wyznaczamy sciezke do początku trasy.
            planPrzejazdu = bfs.wyznaczSciezke(wymarzonaTrasa.poczatek());

            if (planPrzejazdu != null) {
                // Do sciezki do początku trasy dodajemy zjazd wymarzoną trasą.
                planPrzejazdu.add(wymarzonaTrasa);
                return pobierzNastepnyZPlanu(moment);
            }
        }

        return null;
    }

    protected Queue<Krawedz> planPrzejazdu() {
        return planPrzejazdu;
    }

}
