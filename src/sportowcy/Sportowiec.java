package sportowcy;

import czas.Interwal;
import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import losowosc.MaszynaLosujaca;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.historia_przejazdow.HistoriaPrzejazdowSportowca;

public abstract class Sportowiec {

    private static final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI = 5;
    private static final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY = 7;
    private static final double DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY = 0.2;

    private final int id;
    private final int poziomZaawansowania; // {0, 1, ..., 10}
    private final double wspolczynnikSpontanicznosci; // [0, 1]
    private final double wagaTrudnosci; // [0, 1]
    private final double wagaNawierzchni; // [0, 1]
    private final double wagaZnudzenia; // [0, 1]
    private final boolean sledzony;
    private final Wezel wezelStartowy;
    private final Moment momentStartu;
    private final MaszynaLosujaca maszynaLosujaca;
    private final HistoriaPrzejazdowSportowca historiaPrzejazdow;
    private final MiernikZnudzenia miernikZnudzenia;

    public Sportowiec(int id,
                      int poziomZaawansowania,
                      double wspolczynnikSpontanicznosci,
                      double wagaTrudnosci,
                      double wagaNawierzchni,
                      boolean sledzony,
                      Wezel wezelStartowy,
                      Moment momentStartu,
                      MaszynaLosujaca maszynaLosujaca,
                      double wspolczynnikZnudzenia, double wagaZnudzenia) {
        assert Math.abs(wagaTrudnosci + wagaNawierzchni + wagaZnudzenia - 1.0) < 1e-6
                : "Wagi atrakcyjnosci musza sumowac się do 1.";

        assert wspolczynnikSpontanicznosci >= 0.0 && wspolczynnikSpontanicznosci <= 1.0
                : "Wsp spontanicznosci musi być z przedziału [0, 1].";

        this.id = id;
        this.poziomZaawansowania = poziomZaawansowania;
        this.wspolczynnikSpontanicznosci = wspolczynnikSpontanicznosci;
        this.wagaTrudnosci = wagaTrudnosci;
        this.wagaNawierzchni = wagaNawierzchni;
        this.sledzony = sledzony;
        this.wezelStartowy = wezelStartowy;
        this.momentStartu = momentStartu;
        this.maszynaLosujaca = maszynaLosujaca;
        this.wagaZnudzenia = wagaZnudzenia;

        historiaPrzejazdow = new HistoriaPrzejazdowSportowca();
        miernikZnudzenia = new MiernikZnudzenia(wspolczynnikZnudzenia);
    }

    public int id() {
        return id;
    }

    public int poziomZaawansowania() {
        return poziomZaawansowania;
    }

    public double wagaTrudnosci() {
        return wagaTrudnosci;
    }

    public double wagaNawierzchni() {
        return wagaNawierzchni;
    }

    public double wspolczynnikSpontanicznosci() {
        return wspolczynnikSpontanicznosci;
    }

    public boolean sledzony() {
        return sledzony;
    }

    protected MaszynaLosujaca maszynaLosujaca() {
        return maszynaLosujaca;
    }

    public Wezel wezelStartowy() {
        return wezelStartowy;
    }

    public Moment momentStartu() {
        return momentStartu;
    }

    public abstract Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel, Osrodek osrodek);

    public HistoriaPrzejazdowSportowca historiaPrzejazdow() {
        return historiaPrzejazdow;
    }

    /**
     * Funkcja, która rejestruje w historii przejazd TRASĄ sportowca
     * oraz rejestruje poziom znudzenia.
     */
    public void zarejestrujPrzejazdTrasa(Trasa trasa) {
        historiaPrzejazdow.obslozPrzejazd(trasa);
        miernikZnudzenia.zglosZjazdTrasa(trasa);
    }

    /**
     * Funkcja, która rejestruje w historii przejazd WYCIĄGIEM sportowca.
     */
    public void zarejestrujPrzejazdWyciagiem(Wyciag wyciag) {
        historiaPrzejazdow.obslozPrzejazd(wyciag);
    }

    /**
     * Wylicza atrakcyjność trasy na podstawie wzoru z treści zadania.
     */
    protected double lacznaAtrakcyjnosc(Trasa trasa) {
        return wagaTrudnosci * atrakcyjnoscPoziomuTrudnosci(trasa)
                + wagaNawierzchni * trasa.wyrownanieNawierzchni() +
                wagaZnudzenia * (1 - miernikZnudzenia.pobierzZnudzenie(trasa));
    }

    protected double atrakcyjnoscPoziomuTrudnosci(Trasa trasa) {
        int poziomTrudnosci = trasa.poziomTrudnosci();

        double roznicaPoziomow = poziomTrudnosci - poziomZaawansowania;

        if (poziomTrudnosci >= poziomZaawansowania + WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI) {
            return 0;
        } else if (poziomZaawansowania + WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI > poziomTrudnosci
                && poziomTrudnosci >= poziomZaawansowania) {
            return 1.0 - roznicaPoziomow / (double) WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI;
        } else {
            return Math.max(DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY,
                    1 - (-roznicaPoziomow) / (double) WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY);
        }
    }

    /**
     * Funkcja determinująca, czy nastepny krok jest losowy.
     */
    public boolean czyNastepnyKrokLosowy() {
        return maszynaLosujaca.losowyDouble(0, 1) < wspolczynnikSpontanicznosci;
    }

    /**
     * Spontaniczna decyzja polega na wylosowaniu następnej krawędzi jednostajnie
     * spośród wszystkich zaczynających się w obecnym wierzchołku.
     */
    protected Zdarzenie podejmijSpontanicznaDecyzje(Moment moment, Wezel obecnyWezel) {
        final MaszynaLosujaca maszynaLosujaca = maszynaLosujaca();
        Trasa[] bezposrednieTrasy = obecnyWezel.wychodzaceTrasy();
        Wyciag[] wyciagi = obecnyWezel.wychodzaceWyciagi();

        int losowyWybor = maszynaLosujaca.losowyInt(0, bezposrednieTrasy.length + wyciagi.length);

        if (losowyWybor < bezposrednieTrasy.length) {
            return bezposrednieTrasy[losowyWybor].zdarzenieNastepnegoKroku(moment, this);
        } else {
            return wyciagi[losowyWybor - bezposrednieTrasy.length].zdarzenieNastepnegoKroku(moment, this);
        }
    }


    /**
     * Tworzy kopie sportowca z tymi samymi parametrami ale zwiekszonym id oraz momentem startu.
     * Uzywane na potrzeby tworzenia wielu sportowcow z jednej grupy sportowcow z wejscia.
     */
    public SportowiecLokalny kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return new SportowiecLokalny(id + przesuniecieId,
                poziomZaawansowania,
                wspolczynnikSpontanicznosci,
                wagaTrudnosci,
                wagaNawierzchni,
                sledzony,
                wezelStartowy,
                momentStartu.dodajInterwal(przesuniecieMomentuStartu),
                maszynaLosujaca,
                miernikZnudzenia.beta(),
                wagaZnudzenia);
    }

    @Override
    public String toString() {
        return String.format("Sportowiec nr %d", id);
    }

}
