package sportowcy.sportowcy_planujacy;

import czas.Moment;
import losowosc.MaszynaLosujaca;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import sportowcy.sportowcy_planujacy.plan.PrzeszukiwanieGrafu;

import java.util.*;

public class SportowiecKolekcjoner extends SportowiecPlanujacy {

    public SportowiecKolekcjoner(int id,
                             int poziomZaawansowania,
                             double wspolczynnikSpontanicznosci,
                             double wagaTrudnosci,
                             double wagaNawierzchni,
                             boolean sledzony,
                             Wezel wezelStartowy,
                             Moment momentStartu,
                             MaszynaLosujaca maszynaLosujaca,
                             double wspolczynnikZnudzenia, double wagaZnudzenia) {
        super(id, poziomZaawansowania, wspolczynnikSpontanicznosci, wagaTrudnosci, wagaNawierzchni, sledzony, wezelStartowy,
                momentStartu, maszynaLosujaca, wspolczynnikZnudzenia, wagaZnudzenia);
    }

    @Override
    protected Trasa znajdzWymarzonaTrase(Osrodek osrodek, PrzeszukiwanieGrafu bfs) {
        return Arrays.stream(osrodek.trasy())
                .filter(trasa -> bfs.pobierzOdleglosc(trasa.poczatek()) != -1)
                .min(
                    // Min zjazdow
                    Comparator.comparingInt((Trasa trasa) -> historiaPrzejazdow().liczbaPrzejazdowKrawedzia(trasa))
                    // Jesli remis to min dystans
                    .thenComparingInt(trasa -> bfs.pobierzOdleglosc(trasa.poczatek()))
                    // Dalszy remis to max atrakcyjnosc
                    .thenComparing(Comparator.comparingDouble(this::lacznaAtrakcyjnosc).reversed())
                ).orElse(null);
    }

}
