package sportowcy.historia_przejazdow;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class RejestrHistorii {

    private final SortedSet<Integer> indeksy;

    public RejestrHistorii() {
        indeksy = new TreeSet<>();
    }

    public int rozmiar() {
        return indeksy.size();
    }

    public boolean czyPusty() {
        return indeksy.isEmpty();
    }

    /**
     * Zwraca indeks ostatniego zarejestrowanego zjazdu
     * lub 0, jeśli taki jescze nie nastąpił.
     */
    public int indeksOstatniegoWpisu() {
        if (czyPusty()) {
            return 0;
        } else {
            return indeksy.last();
        }
    }

    /**
     * Dodaje przejazd o podanym indeksie do rejestru.
     */
    public void dodaj(int id) {
        indeksy.add(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (final int nr : indeksy) {
            if (sb.isEmpty()) {
                sb.append(nr);
            } else {
                sb.append(",").append(nr);
            }
        }

        return sb.toString();
    }
}
