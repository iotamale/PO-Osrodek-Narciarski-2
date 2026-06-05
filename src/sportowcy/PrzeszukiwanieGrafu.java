package sportowcy;

import czas.Interwal;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;

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

    public Queue<Krawedz> wyznaczPlan(Trasa cel) {
        final Wezel poczatekTrasy = cel.poczatek();

        if (!odleglosci.containsKey(poczatekTrasy)) {
            return null;
        }

        final LinkedList<Krawedz> plan = new LinkedList<>();
        plan.addFirst(cel);

        Wezel obecny = poczatekTrasy;
        while (poprzednicy.containsKey(obecny)) {
            plan.addFirst(krawedzieWejsciowe.get(obecny));
            obecny = poprzednicy.get(obecny);
        }

        return plan;
    }

    public int pobierzOdleglosc(Wezel wezel) {
        return odleglosci.getOrDefault(wezel, -1);
    }




}
