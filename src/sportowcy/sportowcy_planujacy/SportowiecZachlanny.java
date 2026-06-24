package sportowcy.sportowcy_planujacy;

import czas.Moment;
import losowosc.MaszynaLosujaca;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import przeszukiwanie_grafu.PrzeszukiwanieGrafu;

import java.util.Arrays;
import java.util.Comparator;

public class SportowiecZachlanny extends SportowiecPlanujacy {

    public SportowiecZachlanny(int id,
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
    public Trasa znajdzWymarzonaTrase(Osrodek osrodek, PrzeszukiwanieGrafu bfs) {
        return osrodek.trasy().stream()
                // Bierzemy tylko osiagalne trasy
                .filter(trasa -> bfs.pobierzOdleglosc(trasa.poczatek()) != -1)
                .max(Comparator.comparingDouble(this::lacznaAtrakcyjnosc))
                .orElse(null);
    }

}
