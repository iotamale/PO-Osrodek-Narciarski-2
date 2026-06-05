package sportowcy;

import com.sun.source.tree.Tree;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

import java.util.*;

public class HistoriaPrzejazdowSportowca {

    private final Map<Krawedz, SortedSet<Integer>> historia;
    private int licznikPrzejazdow;

    public HistoriaPrzejazdowSportowca() {
        historia = new HashMap<>();
        licznikPrzejazdow = 0;
    }

    private SortedSet<Integer> pobierzPrzejazdy(Krawedz krawedz) {
        return historia.getOrDefault(krawedz, new TreeSet<>());
    }

    public int liczbaPrzejazdowKrawedzia(Krawedz krawedz) {
        return pobierzPrzejazdy(krawedz).size();
    }

    /**
     * Funkcja odpowiedzialna za rejestrowanie historii przejazdów zgłaszanych przez inne Klasy.
     */
    public void obslozPrzejazd(Krawedz krawedz) {
        licznikPrzejazdow++;

        historia.putIfAbsent(krawedz, new TreeSet<>());
        final SortedSet<Integer> set = historia.get(krawedz);
        assert set != null : "Blad pobrania wartosci z setu.";

        set.add(licznikPrzejazdow);
    }

    /**
     * Funkcja znajduje potencjalnych kandydatów na następny cel Sportowca Kolecjonera.
     */
    public HashSet<Trasa> najrzadziejOdwiedzaneTrasy(Osrodek osrodek) {
        final HashSet<Trasa> kandydaci = new HashSet<>();
        int minPrzejazdy = -1;

        for (final Trasa trasa : osrodek.trasy()) {
            final SortedSet<Integer> set = pobierzPrzejazdy(trasa);
            final int liczba = set.size();

            if (minPrzejazdy == -1) {
                minPrzejazdy = liczba;
                kandydaci.add(trasa);
            } else if (liczba < minPrzejazdy) {
                minPrzejazdy = liczba;
                kandydaci.clear();
                kandydaci.add(trasa);
            } else if (liczba == minPrzejazdy) {
                kandydaci.add(trasa);
            }
        }

        return kandydaci;
    }

    // TODO - oddzielna klasa?
    // TODO co gdy size = 0?
    public ArrayList<String> stringDlaKrawedzi(Krawedz krawedz) {
        final StringBuilder sb = new StringBuilder();
        final SortedSet<Integer> set = pobierzPrzejazdy(krawedz);
        sb.append(krawedz);
        sb.append(String.format("(%d): ", set.size()));

        boolean pierwszy = true;
        for (final int nr : set) {
            if (pierwszy) {
                sb.append(nr);
                pierwszy = false;
            } else {
                sb.append(",").append(nr);
            }
        }

        final ArrayList<String> lista = new ArrayList<>();
        lista.add(sb.toString());
        return lista;
    }

}
