package sportowcy;

import czas.Interwal;
import czas.Moment;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import losowosc.MaszynaLosujaca;
import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

import java.util.List;

public class SportowiecLokalny extends Sportowiec {

    public SportowiecLokalny(int id,
                      int poziomZaawansowania,
                      double wspolczynnikSpontanicznosci,
                      double wagaTrudnosci,
                      double wagaNawierzchni,
                      boolean sledzony,
                      Wezel wezelStartowy,
                      Moment momentStartu,
                      MaszynaLosujaca maszynaLosujaca,
                      double wspolczynnikZnudzenia, double wagaZnudzenia) {
        super(id, poziomZaawansowania, wspolczynnikSpontanicznosci, wagaTrudnosci, wagaNawierzchni, sledzony,
                wezelStartowy, momentStartu, maszynaLosujaca, wspolczynnikZnudzenia, wagaZnudzenia);
    }

    /**
     * Dla zadanego obecnego węzła daje wydarzenie opisujace następny
     * krok sportowca: dołączenie do kolejki na wyciągu lub rozpoczęcie zjazdu trasą.
     */
    @Override
    public Zdarzenie nastepnyKrok(Moment moment, Wezel obecnyWezel, Osrodek osrodek) {
        final boolean czySpontaniczna = czyNastepnyKrokLosowy();

        if (czySpontaniczna) {
            return podejmijSpontanicznaDecyzje(moment, obecnyWezel);
        } else {
            return podejmijPrzemyslanaDecyzje(moment, obecnyWezel);
        }
    }

    /**
     * Przemyślana decyzja polega na wybraniu najatrakcyjniejszej trasy spośród sumy tras
     * zaczynających się w obecnym wierzchołku oraz tras których początkowe wierzchołki
     * są osiągalne wyciągiem zaczynającym się w obecnym wierzchołku.
     */
    private Zdarzenie podejmijPrzemyslanaDecyzje(Moment moment, Wezel obecnyWezel) {
        List<Trasa> bezposrednieTrasy = obecnyWezel.wychodzaceTrasy();
        List<Wyciag> wyciagi = obecnyWezel.wychodzaceWyciagi();

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
            return wyciagi.getFirst().zdarzenieNastepnegoKroku(moment, this);
        } else if (nastepnyWyciag == null) {
            // Wybrana trasa zaczyna się w obecnym wierzchołku.
            return najlepszaTrasa.zdarzenieNastepnegoKroku(moment, this);
        } else {
            // Musimy wjechac wyciągiem by dotrzeć do upatrzonej trasy.
            return nastepnyWyciag.zdarzenieNastepnegoKroku(moment, this);
        }
    }

    @Override
    public Sportowiec kopia(int przesuniecieId, Interwal przesuniecieMomentuStartu) {
        return new SportowiecLokalny(id() + przesuniecieId,
                poziomZaawansowania(),
                wspolczynnikSpontanicznosci(),
                wagaTrudnosci(),
                wagaNawierzchni(),
                sledzony(),
                wezelStartowy(),
                momentStartu().dodajInterwal(przesuniecieMomentuStartu),
                maszynaLosujaca(),
                beta(),
                wagaZnudzenia());
    }
}
