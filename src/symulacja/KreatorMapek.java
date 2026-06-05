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
import wczytywacz.DaneWejsciowe;

public class KreatorMapek {

    private static final StylWezla W_GRUBY = new StylWezla(GruboscKonturu.POGRUBIONY);
    private static final StylWezla W_NORMALNY = new StylWezla(GruboscKonturu.ZWYKLY);
    private static final StylKrawedzi K_TRASA = new StylKrawedzi(StylLinii.CIAGLA);
    private static final StylKrawedzi K_WYCIAG = new StylKrawedzi(StylLinii.PRZERYWANA);

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

    public void generujMapkeParametrow() throws WyjatekSystemuPlikow {
        for (final Wezel w : wezly) {
            final StylWezla styl = w.czyStartowy() ? W_GRUBY : W_NORMALNY;
            generator.dodajWezel(w.id(), w.wspolrzednaX(), w.wspolrzednaY(), styl);
        }

        for (final Trasa t : trasy) {
            final int nrPoczatek = t.poczatek().id();
            final int nrKoniec = t.koniec().id();
            generator.dodajKrawedz(nrPoczatek, nrKoniec, K_TRASA, t.generujOpisMapkaParametrow());
        }

        for (final Wyciag w : wyciagi) {
            final int nrPoczatek = w.poczatek().id();
            final int nrKoniec = w.koniec().id();
            generator.dodajKrawedz(nrPoczatek, nrKoniec, K_WYCIAG, w.generujOpisMapkaParametrow());
        }

        generator.tworzMapke("pierwsza.tex");
    }

}
