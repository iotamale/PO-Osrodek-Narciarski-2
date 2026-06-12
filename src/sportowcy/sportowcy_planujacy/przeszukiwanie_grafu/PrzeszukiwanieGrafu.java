package sportowcy.sportowcy_planujacy.przeszukiwanie_grafu;

import osrodek.Wezel;
import osrodek.krawedz.Krawedz;

import java.util.Queue;

public interface PrzeszukiwanieGrafu {

    /**
     * Wyznacza ścieżkę do docelowego węzła.
     * Jeśli taka scieżka nie istnieje, to zwraca null.
     */
    public Queue<Krawedz> wyznaczSciezke(Wezel cel);

    /**
     * Zwraca odległość z węzła startowego do węzła docelowego.
     * Jeśli taka ścieżka nie istnieje, to zwraca -1.
     */
    public int pobierzOdleglosc(Wezel cel);

}
