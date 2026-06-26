package sportowcy.sportowcy_planujacy;

import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import losowosc.MaszynaLosujaca;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import sportowcy.Sportowiec;
import przeszukiwanie_grafu.PlanPrzejazdu;
import przeszukiwanie_grafu.PrzeszukiwanieGrafu;

public abstract class SportowiecPlanujacy extends Sportowiec {

    private final PlanPrzejazdu planPrzejazdu;

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
        super(id, poziomZaawansowania, wspolczynnikSpontanicznosci, wagaTrudnosci, wagaNawierzchni, sledzony,
                wezelStartowy, momentStartu, maszynaLosujaca, wspolczynnikZnudzenia, wagaZnudzenia);
        planPrzejazdu = new PlanPrzejazdu(this);
    }

    /**
     * Funkcja odpowiedzialna za wyznaczenie najlepszej trasy dla planującego sportowca.
     */
    public abstract Trasa znajdzWymarzonaTrase(Osrodek osrodek, PrzeszukiwanieGrafu bfs);

    @Override
    public Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel, Osrodek osrodek) {
        // Zmiana planu i spontanicznosc sa dostepne tylko, gdy poprzedni plan zostal zrealizowany.
        if (!planPrzejazdu.czyPusta()) {
            return planPrzejazdu.pobierzZdarzenieNastepnegoKroku(moment);
        }

        // Nie mamy planu to mozemy zachowac sie spontanicznie.
        if (czyNastepnyKrokLosowy()) {
            return podejmijSpontanicznaDecyzje(moment, obecnyWezel);
        }

        // Dobór przez BFS - wybór trasy delegowany do PlanPrzejazdu.
        planPrzejazdu.przygotuj(obecnyWezel, osrodek);

        return planPrzejazdu.pobierzZdarzenieNastepnegoKroku(moment);
    }

}
