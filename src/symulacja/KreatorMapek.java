package symulacja;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.Sportowiec;
import symulacja.odwiedzajacy.OdwiedzajacyMapkiParametrow;
import symulacja.odwiedzajacy.OdwiedzajacyMapkiSportowcow;
import symulacja.odwiedzajacy.OdwiedzajacyMapkiStatystyk;
import wczytywacz.DaneWejsciowe;

import java.util.List;
import java.util.function.Function;

public class KreatorMapek {

    private static final StylWezla W_GRUBY = new StylWezla(GruboscKonturu.POGRUBIONY);
    private static final StylWezla W_NORMALNY = new StylWezla(GruboscKonturu.ZWYKLY);
    private static final StylKrawedzi K_TRASA = new StylKrawedzi(StylLinii.CIAGLA);
    private static final StylKrawedzi K_WYCIAG = new StylKrawedzi(StylLinii.PRZERYWANA);
    private static final String NAZWA_PLIK_PARAMETRY = "parametry.tex";
    private static final String NAZWA_PLIK_STATYSTYKI = "statystyki.tex";
    private static final String PREFIX_PLIK_SPORTOWIEC = "sportowiec";
    private static final String POSTFIX_PLIK_SPORTOWIEC = ".tex";

    private final GeneratorMapek generator;
    private final List<Sportowiec> sportowcy;
    private final List<Trasa> trasy;
    private final List<Wyciag> wyciagi;
    private final List<Wezel> wezly;

    public KreatorMapek(String katalog, DaneWejsciowe dane) throws WyjatekSystemuPlikow {
        generator = new GeneratorMapek(katalog);
        sportowcy = dane.sportowcy();
        trasy = dane.osrodek().trasy();
        wyciagi = dane.osrodek().wyciagi();
        wezly = dane.osrodek().wezly();
    }

    /**
     * Generuje nazwe pliku .tex dla sportowca.
     */
    private String nazwaPlikuSportowca(Sportowiec sportowiec) {
        return PREFIX_PLIK_SPORTOWIEC + "-" + sportowiec.id() + POSTFIX_PLIK_SPORTOWIEC;
    }

    /**
     * Funkcja odpowiedzialna za generowanie mapek. Dzięki interfejsowi KreatorKrawedzi
     * unikamy podwojnej deklaracji funkcji w zaleznosci od tego, czy korzystamy
     * z generatorMapek::dodajKrawedz(List<String>), czy z jego przeciążonej wersji
     * przyjmujacej String.
     */
    private <T> void generujMapke(Function<Trasa, T> ekstraktorTrasa,
                              Function<Wyciag, T> ekstraktorWyciag,
                              KreatorKrawedzi<T> funkcja,
                              String nazwaPliku) throws WyjatekSystemuPlikow {
        generator.zeruj();
        dodajWszystkieWezly();

        for (final Trasa t : trasy) {
            final int nrPoczatek = t.poczatek().id();
            final int nrKoniec = t.koniec().id();
            funkcja.dodaj(nrPoczatek, nrKoniec, K_TRASA, ekstraktorTrasa.apply(t));
        }

        for (final Wyciag w : wyciagi) {
            final int nrPoczatek = w.poczatek().id();
            final int nrKoniec = w.koniec().id();
            funkcja.dodaj(nrPoczatek, nrKoniec, K_WYCIAG, ekstraktorWyciag.apply(w));
        }

        generator.tworzMapke(nazwaPliku);
    }

    private void dodajWszystkieWezly() {
        for (final Wezel w : wezly) {
            final StylWezla styl = w.czyStartowy() ? W_GRUBY : W_NORMALNY;
            generator.dodajWezel(w.id(), w.wspolrzednaX(), w.wspolrzednaY(), styl);
        }
    }

    public void generujMapkeParametrow() throws WyjatekSystemuPlikow {
        final OdwiedzajacyMapkiParametrow odwiedzajacy = new OdwiedzajacyMapkiParametrow();
        generujMapke(trasa -> trasa.przyjmij(odwiedzajacy), wyciag -> wyciag.przyjmij(odwiedzajacy),
                generator::dodajKrawedz, NAZWA_PLIK_PARAMETRY);
    }

    public void generujMapkeStatystyk() throws WyjatekSystemuPlikow {
        final OdwiedzajacyMapkiStatystyk odwiedzajacy = new OdwiedzajacyMapkiStatystyk();
        generujMapke(trasa -> trasa.przyjmij(odwiedzajacy), wyciag -> wyciag.przyjmij(odwiedzajacy),
                generator::dodajKrawedz, NAZWA_PLIK_STATYSTYKI);
    }

    public void generujMapkeSportowcow() throws WyjatekSystemuPlikow {
        for (final Sportowiec s : sportowcy) {
            if (!s.sledzony()) {
                continue;
            }
            final OdwiedzajacyMapkiSportowcow odwiedzajacy = new OdwiedzajacyMapkiSportowcow(s.historiaPrzejazdow());
            generujMapke(trasa -> trasa.przyjmij(odwiedzajacy), wyciag -> wyciag.przyjmij(odwiedzajacy),
                    generator::dodajKrawedz, nazwaPlikuSportowca(s));
        }
    }

    /**
     * Funkcja generująca wszystkie mapki.
     */
    public void generujWszystkieMapki() throws WyjatekSystemuPlikow {
        generujMapkeParametrow();
        generujMapkeStatystyk();
        generujMapkeSportowcow();
    }

}
