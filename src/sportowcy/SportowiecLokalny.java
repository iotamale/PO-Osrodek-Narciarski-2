package sportowcy;

import czas.Interwal;
import czas.Moment;
import kolejkaZdarzen.zdarzenia.DolaczenieDoKolejki;
import kolejkaZdarzen.zdarzenia.RozpoczecieZjazdu;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import losowosc.MaszynaLosujaca;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

public class SportowiecKlasyczny extends Sportowiec {

    private static final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI = 5;
    private static final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY = 7;
    private static final double DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY = 0.2;

    private final int id;
    private final int poziomZaawansowania; // {0, 1, ..., 10}
    private final double wspolczynnikSpontanicznosci; // [0, 1]
    private final double wspolczynnikTrudnosci; // [0, 1]
    private final double wspolczynnikNawierzchni; // [0, 1]
    private final boolean sledzony;
    private final Wezel wezelStartowy;
    private final Moment momentStartu;
    private final MaszynaLosujaca maszynaLosujaca;

    public SportowiecKlasyczny(int id,
                               int poziomZaawansowania,
                               double wspolczynnikSpontanicznosci,
                               double wspolczynnikTrudnosci,
                               double wspolczynnikNawierzchni,
                               boolean sledzony,
                               Wezel wezelStartowy,
                               Moment momentStartu,
                               MaszynaLosujaca maszynaLosujaca) {
        this.id = id;
        this.poziomZaawansowania = poziomZaawansowania;
        this.wspolczynnikSpontanicznosci = wspolczynnikSpontanicznosci;
        this.wspolczynnikTrudnosci = wspolczynnikTrudnosci;
        this.wspolczynnikNawierzchni = wspolczynnikNawierzchni;
        this.sledzony = sledzony;
        this.wezelStartowy = wezelStartowy;
        this.momentStartu = momentStartu;
        this.maszynaLosujaca = maszynaLosujaca;
    }

    public int id() {
        return id;
    }

    public int poziomZaawansowania() {
        return poziomZaawansowania;
    }

    public double wspolczynnikTrudnosci() {
        return wspolczynnikTrudnosci;
    }

    public double wspolczynnikNawierzchni() {
        return wspolczynnikNawierzchni;
    }

    public boolean sledzony() {
        return sledzony;
    }

    public Wezel wezelStartowy() {
        return wezelStartowy;
    }

    public Moment momentStartu() {
        return momentStartu;
    }

    /**
     * Dla zadanego obecnego węzła daje wydarzenie opisujace następny
     * krok sportowca: dołączenie do kolejki na wyciągu lub rozpoczęcie zjazdu trasą.
     */
    public Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel) {
        if (maszynaLosujaca.losowyDouble(0, 1) < wspolczynnikSpontanicznosci) {
            return podejmijSpontanicznaDecyzje(moment, obecnyWezel);
        } else {
            return podejmijPrzemyslanaDecyzje(moment, obecnyWezel);
        }
    }

    /**
     * Spontaniczna decyzja polega na wylosowaniu następnej krawędzi jednostajnie
     * spośród wszystkich zaczynających się w obecnym wierzchołku.
     */
    private Zdarzenie podejmijSpontanicznaDecyzje(Moment moment, Wezel obecnyWezel) {
        Trasa[] bezposrednieTrasy = obecnyWezel.wychodzaceTrasy();
        Wyciag[] wyciagi = obecnyWezel.wychodzaceWyciagi();

        int losowyWybor = maszynaLosujaca.losowyInt(0, bezposrednieTrasy.length + wyciagi.length);

        if (losowyWybor < bezposrednieTrasy.length) {
            return nastepnyKrokTrasa(moment, bezposrednieTrasy[losowyWybor]);
        } else {
            return nastepnyKrokWyciag(moment, wyciagi[losowyWybor - bezposrednieTrasy.length]);
        }
    }

    /**
     * Przemyślana decyzja polega na wybraniu najatrakcyjniejszej trasy spośród sumy tras
     * zaczynających się w obecnym wierzchołku oraz tras których początkowe wierzchołki
     * są osiągalne wyciągiem zaczynającym się w obecnym wierzchołku.
     */
    private Zdarzenie podejmijPrzemyslanaDecyzje(Moment moment, Wezel obecnyWezel) {
        Trasa[] bezposrednieTrasy = obecnyWezel.wychodzaceTrasy();
        Wyciag[] wyciagi = obecnyWezel.wychodzaceWyciagi();

        double najwiekszaAtrakcyjnosc = -1;
        Trasa najlepszaTrasa = null;
        Wyciag nastepnyWyciag = null;

        for (Trasa trasa : bezposrednieTrasy) {
            double atrakcyjnosc = lacznaAtrakcyjnosc(trasa);
            if (atrakcyjnosc > najwiekszaAtrakcyjnosc) {
                najwiekszaAtrakcyjnosc = atrakcyjnosc;
                najlepszaTrasa = trasa;
            }
        }

        for (Wyciag wyciag : wyciagi) {
            for (Trasa trasa : wyciag.koniec().wychodzaceTrasy()) {
                double atrakcyjnosc = lacznaAtrakcyjnosc(trasa);
                if (atrakcyjnosc > najwiekszaAtrakcyjnosc) {
                    najwiekszaAtrakcyjnosc = atrakcyjnosc;
                    najlepszaTrasa = trasa;
                    nastepnyWyciag = wyciag;
                }
            }
        }

        if (najlepszaTrasa == null) {
            // Zbiór dostępnych tras jest pusty, wiec wybieramy dowolny wyciąg.
            // Mamy gwarancję że taki istnieje, ponieważ graf jest silnie spójny.
            return nastepnyKrokWyciag(moment, wyciagi[0]);
        } else if (nastepnyWyciag == null) {
            // Wybrana trasa zaczyna się w obecnym wierzchołku.
            return nastepnyKrokTrasa(moment, najlepszaTrasa);
        } else {
            // Musimy wjechac wyciągiem by dotrzeć do upatrzonej trasy.
            return nastepnyKrokWyciag(moment, nastepnyWyciag);
        }
    }

    /**
     * Tworzy zdarzenie dołączenia do kolejki do wyciągu w nastepnym kroku.
     */
    private DolaczenieDoKolejki nastepnyKrokWyciag(Moment moment, Wyciag wyciag) {
        return new DolaczenieDoKolejki(moment, wyciag, this);
    }

    /**
     * Tworzy zdarzenie zjazdu bezpośrednią trasą w następnym kroku.
     */
    private RozpoczecieZjazdu nastepnyKrokTrasa(Moment moment, Trasa trasa) {
        return new RozpoczecieZjazdu(moment, trasa, this);
    }

    /**
     * Wylicza atrakcyjność trasy na podstawie wzoru z treści zadania.
     */
    private double lacznaAtrakcyjnosc(Trasa trasa) {
        return wspolczynnikTrudnosci * atrakcyjnoscPoziomuTrudnosci(trasa)
            + wspolczynnikNawierzchni * trasa.wyrownanieNawierzchni();
    }

    private double atrakcyjnoscPoziomuTrudnosci(Trasa trasa) {
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
     * Tworzy kopie sportowca z tymi samymi parametrami ale zwiekszonym id oraz momentem startu.
     * Uzywane na potrzeby tworzenia wielu sportowcow z jednej grupy sportowcow z wejscia.
     */
    public SportowiecKlasyczny kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return new SportowiecKlasyczny(id + przesuniecieId,
            poziomZaawansowania,
            wspolczynnikSpontanicznosci,
            wspolczynnikTrudnosci,
            wspolczynnikNawierzchni,
            sledzony,
            wezelStartowy,
            momentStartu.dodajInterwal(przesuniecieMomentuStartu),
            maszynaLosujaca);
    }

    @Override
    public String toString() {
        return String.format("Sportowiec nr %d", id);
    }
}
