package kolejkaZdarzen;

import kolejkaZdarzen.zdarzenia.Zdarzenie;

class ParaZdarzeniowa {

    private static long globalnyLicznik = Long.MIN_VALUE;

    private final Zdarzenie zdarzenie;
    private final long numerPorzadkowy;

    protected ParaZdarzeniowa(Zdarzenie zdarzenie) {
        this.zdarzenie = zdarzenie;
        this.numerPorzadkowy = globalnyLicznik++;
    }

    protected Zdarzenie zdarzenie() {
        return zdarzenie;
    }

    protected int porownajNumery(ParaZdarzeniowa o) {
        return Long.compare(this.numerPorzadkowy, o.numerPorzadkowy);
    }

}
