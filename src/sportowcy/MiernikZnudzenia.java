package sportowcy;

import osrodek.krawedz.Trasa;

import java.util.HashMap;
import java.util.Map;

public class MiernikZnudzenia {

    private final double beta;
    private final Map<Trasa, Double> historiaZnudzenia;
    private final Map<Trasa, Integer> ostatniaAktulizacja;  // todo czy nie lepiej korzystac z historii?
    private int globalnyLicznikZjazdow;

    public MiernikZnudzenia(double beta) {
        this.beta = beta;
        historiaZnudzenia = new HashMap<>();
        ostatniaAktulizacja = new HashMap<>();
        globalnyLicznikZjazdow = 0;
    }

    /**
     * Zwraca aktualne znudzenie.
     */
    public double pobierzZnudzenie(Trasa trasa) {
        final double poprzZnudzenie = historiaZnudzenia.getOrDefault(trasa, 0.0);
        final int poprzCzas = ostatniaAktulizacja.getOrDefault(trasa, 0);

        final int opuszczoneZjazdy = globalnyLicznikZjazdow - poprzCzas;

        return poprzZnudzenie * Math.pow(1.0 - beta, opuszczoneZjazdy);
    }

    /**
     * Aktulizuje stan PO zjechaniu daną trasą.
     */
    public void zglosZjazdTrasa(Trasa trasa) {
        final double znudzeniePrzed = pobierzZnudzenie(trasa);

        globalnyLicznikZjazdow++;

        final double znudzeniePo = beta + (1.0 - beta) * znudzeniePrzed;

        historiaZnudzenia.put(trasa, znudzeniePo);
        ostatniaAktulizacja.put(trasa, globalnyLicznikZjazdow);
    }

    public double beta() {
        return beta;
    }
}
