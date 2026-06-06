package symulacja;

import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.HistoriaPrzejazdowSportowca;
import sportowcy.Sportowiec;
import wczytywacz.DaneWejsciowe;

import java.util.ArrayList;
import java.util.function.Function;

public class KreatorMapek {

    private static final StylWezla W_GRUBY = new StylWezla(GruboscKonturu.POGRUBIONY);
    private static final StylWezla W_NORMALNY = new StylWezla(GruboscKonturu.ZWYKLY);
    private static final StylKrawedzi K_TRASA = new StylKrawedzi(StylLinii.CIAGLA);
    private static final StylKrawedzi K_WYCIAG = new StylKrawedzi(StylLinii.PRZERYWANA);
    private static final String NAZWA_PLIK_PARAMETRY = "parametry.tex";
    private static final String NAZWA_PLIK_STATYSTYKI = "statystyki.tex";
    private static final String PREFIX_PLIK_SPORTOWIEC = "sportowiec";

    private final GeneratorMapek generator;
    private final Sportowiec[] sportowcy;
    private final Trasa[] trasy;
    private final Wyciag[] wyciagi;
    private final Wezel[] wezly;

    public KreatorMapek(String katalog, DaneWejsciowe dane) throws WyjatekSystemuPlikow {
        generator = new GeneratorMapek(katalog);
        sportowcy = dane.sportowcy();
        trasy = dane.osrodek().trasy();
        wyciagi = dane.osrodek().wyciagi();
        wezly = dane.osrodek().wezly();
    }

    private String nazwaPlikuSportowca(Sportowiec sportowiec) {
        return PREFIX_PLIK_SPORTOWIEC + "-" + sportowiec.id() + ".tex";
    }

    private void generujMapke(Function<Trasa, ArrayList<String>> ekstraktorTrasa,
                              Function<Wyciag, ArrayList<String>> ekstraktorWyciag,
                              String nazwaPliku) throws WyjatekSystemuPlikow {
        generator.zeruj();
        dodajWszystkieWezly();

        for (final Trasa t : trasy) {
            final int nrPoczatek = t.poczatek().id();
            final int nrKoniec = t.koniec().id();
            generator.dodajKrawedz(nrPoczatek, nrKoniec, K_TRASA, ekstraktorTrasa.apply(t));
        }

        for (final Wyciag w : wyciagi) {
            final int nrPoczatek = w.poczatek().id();
            final int nrKoniec = w.koniec().id();
            generator.dodajKrawedz(nrPoczatek, nrKoniec, K_WYCIAG, ekstraktorWyciag.apply(w));
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
        generujMapke(Trasa::generujOpisMapkaParametrow, Wyciag::generujOpisMapkaParametrow, NAZWA_PLIK_PARAMETRY);
    }

    public void generujMapkeStatystyk() throws WyjatekSystemuPlikow {
        generujMapke(Trasa::generujOpisMapkaStatystyk, Wyciag::generujOpisMapkaStatystyk, NAZWA_PLIK_STATYSTYKI);
    }

    public void generujMapkeSportowcow() throws WyjatekSystemuPlikow {
        for (final Sportowiec s : sportowcy) {
            if (!s.sledzony()) {
                continue;
            }
            final HistoriaPrzejazdowSportowca historia = s.historiaPrzejazdow();
            generujMapke(historia::stringDlaKrawedzi, historia::stringDlaKrawedzi, nazwaPlikuSportowca(s));
        }
    }

    public void generujWszystkie() throws WyjatekSystemuPlikow {
        generujMapkeParametrow();
        generujMapkeStatystyk();
        generujMapkeSportowcow();
    }

}
