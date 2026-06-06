package sportowcy;

import osrodek.Wezel;
import osrodek.krawedz.Krawedz;

import java.util.*;
import java.util.stream.Stream;

public class PrzeszukiwanieGrafu {

    private final Map<Wezel, Integer> odleglosci;
    private final Map<Wezel, Krawedz> krawedzieWejsciowe;
    private final Map<Wezel, Wezel> poprzednicy;

    public PrzeszukiwanieGrafu(Wezel start) {
        odleglosci = new HashMap<>();
        krawedzieWejsciowe = new HashMap<>();
        poprzednicy = new HashMap<>();

        final Queue<Wezel> kolejka = new ArrayDeque<>();
        kolejka.add(start);
        odleglosci.put(start, 0);

        while (!kolejka.isEmpty()) {
            final Wezel u = kolejka.poll();

            Stream.concat(Arrays.stream(u.wychodzaceTrasy()), Arrays.stream(u.wychodzaceWyciagi()))
                    .forEach(krawedz -> {
                        final Wezel v = krawedz.koniec();

                        if (!odleglosci.containsKey(v)) {
                            odleglosci.put(v, odleglosci.get(u) + 1);
                            krawedzieWejsciowe.put(v, krawedz);
                            poprzednicy.put(v, u);
                            kolejka.add(v);
                        }
                    });
        }
    }

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

    public int pobierzOdleglosc(Wezel wezel) {
        return odleglosci.getOrDefault(wezel, -1);
    }




}
