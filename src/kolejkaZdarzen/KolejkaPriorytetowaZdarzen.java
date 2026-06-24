package kolejkaZdarzen;

import kolejkaZdarzen.zdarzenia.Zdarzenie;

import java.util.PriorityQueue;

public class KolejkaPriorytetowaZdarzen implements KolejkaZdarzen {

    private final PriorityQueue<ParaZdarzeniowa> kolejka;
    private long licznik = Long.MIN_VALUE;

    public KolejkaPriorytetowaZdarzen() {
        this.kolejka = new PriorityQueue<>(new ComparatorParZdarzen());
    }

    @Override
    public void dodaj(Zdarzenie zdarzenie) {
        assert zdarzenie != null : "Zdarzenie dodawane do kolejki nie moze byc nullem";

        kolejka.add(new ParaZdarzeniowa(zdarzenie, licznik++));
    }

    @Override
    public Zdarzenie zdejmij() {
        assert !czyPusta() : "Próba zdjęcia elementu z pustej kolejki";

        final ParaZdarzeniowa top = kolejka.poll();
        assert top != null : "Na czele kolejki odłożony jest null";

        return top.zdarzenie();
    }

    @Override
    public boolean czyPusta() {
        return kolejka.isEmpty();
    }
}
