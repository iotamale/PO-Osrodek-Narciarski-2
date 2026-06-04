package kolejkaZdarzen;

import kolejkaZdarzen.zdarzenia.Zdarzenie;

import java.util.PriorityQueue;

public class KolejkaPriorytetowaZdarzen implements KolejkaZdarzen {

    private final PriorityQueue<ParaZdarzeniowa> kolejka;

    public KolejkaPriorytetowaZdarzen() {
        this.kolejka = new PriorityQueue<>(new ComparatorParZdarzen());
    }

    @Override
    public void dodaj(Zdarzenie zdarzenie) {
        kolejka.add(new ParaZdarzeniowa(zdarzenie));
    }

    @Override
    public Zdarzenie zdejmij() {
        assert !czyPusta() : "Nie można zdjąć elementu z pustej kolejki";

        final ParaZdarzeniowa top = kolejka.poll();
        assert top != null : "Na czele kolejki odłożony jest null";

        return top.zdarzenie();
    }

    @Override
    public boolean czyPusta() {
        return kolejka.isEmpty();
    }
}
