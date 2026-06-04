package sportowcy;

import czas.Interwal;
import czas.Moment;
import losowosc.MaszynaLosujaca;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Sportowiec {

    private static final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI = 5;
    private static final int WSPOLCZYNNIK_ROZNICY_POZIOMOW_TRUDNOSCI_LATWEJ_TRASY = 7;
    private static final double DOMYSLNA_ATRAKCYJNOSC_LATWEJ_TRASY = 0.2;

    private final int id;
    private final int poziomZaawansowania; // {0, 1, ..., 10}
    private final double wspolczynnikSpontanicznosci; // [0, 1]
    private final double wspolczynnikTrudnosci; // [0, 1]
    private final double wspolczynnikNawierzchni; // [0, 1]
    private final double wspolczynnikZnudzenia; // [0, 1]
    private final boolean sledzony;
    private final Wezel wezelStartowy;
    private final Moment momentStartu;
    private final MaszynaLosujaca maszynaLosujaca;
    private final Map<Krawedz, SortedSet<Integer>> historiaPrzejazdow;
    private int licznikPrzejazdowTrasa;
    private int licznikPrzejazdowWyciag;

    public Sportowiec(int id,
                      int poziomZaawansowania,
                      double wspolczynnikSpontanicznosci,
                      double wspolczynnikTrudnosci,
                      double wspolczynnikNawierzchni,
                      boolean sledzony,
                      Wezel wezelStartowy,
                      Moment momentStartu,
                      MaszynaLosujaca maszynaLosujaca,
                      double wspolczynnikZnudzenia) {
        this.id = id;
        this.poziomZaawansowania = poziomZaawansowania;
        this.wspolczynnikSpontanicznosci = wspolczynnikSpontanicznosci;
        this.wspolczynnikTrudnosci = wspolczynnikTrudnosci;
        this.wspolczynnikNawierzchni = wspolczynnikNawierzchni;
        this.sledzony = sledzony;
        this.wezelStartowy = wezelStartowy;
        this.momentStartu = momentStartu;
        this.maszynaLosujaca = maszynaLosujaca;
        this.wspolczynnikZnudzenia = wspolczynnikZnudzenia;
        licznikPrzejazdowTrasa = licznikPrzejazdowWyciag = 0;
        historiaPrzejazdow = new HashMap<>();
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

    public void zglosPrzejazdTrasa(Trasa trasa) {
        licznikPrzejazdowTrasa++;

        historiaPrzejazdow.putIfAbsent(trasa, new TreeSet<>());
        final SortedSet<Integer> set = historiaPrzejazdow.get(trasa);
        assert set != null : "Blad pobrania wartosci z setu.";

        set.add(licznikPrzejazdowTrasa);
    }

    public void zglosPrzejazdWyciagiem(Wyciag wyciag) {
        licznikPrzejazdowWyciag++;

        historiaPrzejazdow.putIfAbsent(wyciag, new TreeSet<>());
        final SortedSet<Integer> set = historiaPrzejazdow.get(wyciag);
        assert set != null : "Blad pobrania wartosci z setu.";

        set.add(licznikPrzejazdowWyciag);
    }

    /**
     * Wylicza atrakcyjność trasy na podstawie wzoru z treści zadania.
     */
    protected double lacznaAtrakcyjnosc(Trasa trasa) {
        return wspolczynnikTrudnosci * atrakcyjnoscPoziomuTrudnosci(trasa)
                + wspolczynnikNawierzchni * trasa.wyrownanieNawierzchni();
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
     * Tworzy kopie sportowca z tymi samymi parametrami ale zwiekszonym id oraz momentem startu.
     * Uzywane na potrzeby tworzenia wielu sportowcow z jednej grupy sportowcow z wejscia.
     */
    public SportowiecLokalny kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return new SportowiecLokalny(id + przesuniecieId,
                poziomZaawansowania,
                wspolczynnikSpontanicznosci,
                wspolczynnikTrudnosci,
                wspolczynnikNawierzchni,
                sledzony,
                wezelStartowy,
                momentStartu.dodajInterwal(przesuniecieMomentuStartu),
                maszynaLosujaca,
                wspolczynnikZnudzenia);
    }

    @Override
    public String toString() {
        return String.format("Sportowiec nr %d", id);
    }

}
