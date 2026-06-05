package symulacja;

import czas.Moment;
import dziennik.Dziennik;
import kolejkaZdarzen.KolejkaZdarzen;
import kolejkaZdarzen.zdarzenia.OdjazdWyciagu;
import kolejkaZdarzen.zdarzenia.PoczatekDnia;
import kolejkaZdarzen.zdarzenia.Zdarzenie;
import osrodek.Osrodek;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.Sportowiec;

public class Symulacja {

    private static final Moment POCZATEK_DNIA = new Moment(9, 0, 0);

    private static final Moment KONIEC_SYMULACJI = new Moment(15, 0, 0);

    public void przeprowadzSymulacje(Dziennik dziennik,
        KolejkaZdarzen kolejkaZdarzen,
        Osrodek osrodek,
         Sportowiec[] sportowcy) {
        przygotujPoczatkoweZdarzenia(kolejkaZdarzen, osrodek, sportowcy);
        glownaPetla(kolejkaZdarzen, dziennik, osrodek);
        zbierzStatystyki(osrodek, dziennik);
    }

    /**
     * Inicjuje kolejkę zdarzeń poprzez wrzucenie na nią początkowych zdarzeń:
     * 1. Początek dnia na stoku dla każdego sportowca zgodnie z jego momentem startu.
     * 2. Pierwszy odjazd wagonika dla każdego wyciągu dokładnie o 9:00:00.
     */
    private void przygotujPoczatkoweZdarzenia(KolejkaZdarzen kolejkaZdarzen, Osrodek osrodek, Sportowiec[] sportowcy) {
        for (Sportowiec sportowiec : sportowcy) {
            kolejkaZdarzen.dodaj(new PoczatekDnia(sportowiec.momentStartu(), sportowiec.wezelStartowy(), sportowiec));
        }
        for (Wyciag wyciag : osrodek.wyciagi()) {
            kolejkaZdarzen.dodaj(new OdjazdWyciagu(POCZATEK_DNIA, wyciag));
        }
    }

    /**
     * Dopóki kolejka zdarzeń nie jest pusta, kolejno:
     * 1. Zdejmujemy następne zdarzenie z kolejki,
     * 2. Przetwarzamy je,
     * 3. Dorzucamy na kolejkę nowe zdarzenia, o ile chcemy je jeszcze przetwarzać.
     */
    private void glownaPetla(KolejkaZdarzen kolejkaZdarzen, Dziennik dziennik, Osrodek osrodek) {
        while (!kolejkaZdarzen.czyPusta()) {
            Zdarzenie nastepneZdarzenie = kolejkaZdarzen.zdejmij();
            Zdarzenie[] noweZdarzenia = nastepneZdarzenie.przetworz(dziennik, osrodek);

            for (Zdarzenie noweZdarzenie : noweZdarzenia) {
                if (noweZdarzenie.moment().wczesniejNiz(KONIEC_SYMULACJI)
                    || noweZdarzenie.czyPrzetwarzacPoZakonczeniuSymulacji()) {
                    kolejkaZdarzen.dodaj(noweZdarzenie);
                }
            }
        }
    }

    /**
     * Wypisuje statystyki końcowe symulacji.
     */
    private void zbierzStatystyki(Osrodek osrodek, Dziennik dziennik) {
        String[][] statystyki = new String[osrodek.trasy().length + osrodek.wyciagi().length][2];

        for (int i = 0; i < osrodek.trasy().length; i++) {
            statystyki[i] = new String[]{osrodek.trasy()[i].toString(), osrodek.trasy()[i].wypiszStatystyki()};
        }
        for (int i = 0; i < osrodek.wyciagi().length; i++) {
            statystyki[osrodek.trasy().length + i] = new String[]{osrodek.wyciagi()[i].toString(),
                osrodek.wyciagi()[i].wypiszStatystyki()};
        }

        dziennik.dodajTabele(statystyki);
    }
}
