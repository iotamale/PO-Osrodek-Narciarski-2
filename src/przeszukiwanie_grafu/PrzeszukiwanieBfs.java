package przeszukiwanie_grafu;

import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import java.util.*;

public class PrzeszukiwanieBfs implements PrzeszukiwanieGrafu {

    private final Map<Wezel, Integer> odleglosci;
    private final Map<Wezel, Krawedz> krawedzieWejsciowe;
    private final Map<Wezel, Wezel> poprzednicy;

    /**
     * Inicjuje BFS od podanego wezla startowego.
     */
    public PrzeszukiwanieBfs(Wezel start) {
        odleglosci = new HashMap<>();
        krawedzieWejsciowe = new HashMap<>();
        poprzednicy = new HashMap<>();

        final Queue<Wezel> kolejka = new ArrayDeque<>();
        kolejka.add(start);
        odleglosci.put(start, 0);

        while (!kolejka.isEmpty()) {
            final Wezel u = kolejka.poll();

            for (final Krawedz krawedz : u.wychodzaceTrasy()) {
                przetworzSasiada(u, krawedz, kolejka);
            }

            for (final Krawedz krawedz : u.wychodzaceWyciagi()) {
                przetworzSasiada(u, krawedz, kolejka);
            }
        }
    }

    /**
     * Funkcja pomocnicza przetwarzająca pojedyńczą krawedz wychodzącą z danego węzła.
     * Jeśli docelowy wezel nie byl jeszcze odwiedzony, zapisuje jego parametry i wrzuca
     * go do kolejki.
     */
    private void przetworzSasiada(Wezel obecny, Krawedz krawedz, Queue<Wezel> kolejka) {
        final Wezel v = krawedz.koniec();

        if (!odleglosci.containsKey(v)) {
            odleglosci.put(v, odleglosci.get(obecny) + 1);
            krawedzieWejsciowe.put(v, krawedz);
            poprzednicy.put(v, obecny);
            kolejka.add(v);
        }
    }

    @Override
    public Queue<Krawedz> wyznaczSciezke(Wezel cel) {
        assert cel != null : "Docelowy wezel nie moze byc nullptr";
        if (!odleglosci.containsKey(cel)) {
            return null;
        }

        final LinkedList<Krawedz> sciezka = new LinkedList<>();
        Wezel obecny = cel;

        while (poprzednicy.containsKey(obecny)) {
            sciezka.addFirst(krawedzieWejsciowe.get(obecny));
            obecny = poprzednicy.get(obecny);
        }

        return sciezka;
    }

    @Override
    public int pobierzOdleglosc(Wezel cel) {
        return odleglosci.getOrDefault(cel, -1);
    }

}
