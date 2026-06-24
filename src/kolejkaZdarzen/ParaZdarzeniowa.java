package kolejkaZdarzen;

import kolejkaZdarzen.zdarzenia.Zdarzenie;

class ParaZdarzeniowa {

    private final Zdarzenie zdarzenie;
    private final long numerPorzadkowy;

    protected ParaZdarzeniowa(Zdarzenie zdarzenie, long numerPorzadkowy) {
        this.zdarzenie = zdarzenie;
        this.numerPorzadkowy = numerPorzadkowy;
    }

    protected Zdarzenie zdarzenie() {
        return zdarzenie;
    }

    protected int porownajNumery(ParaZdarzeniowa o) {
        return Long.compare(this.numerPorzadkowy, o.numerPorzadkowy);
    }

}
