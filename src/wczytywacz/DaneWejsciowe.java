package wczytywacz;

import java.util.Arrays;

import osrodek.Osrodek;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.GrupaSportowcow;
import sportowcy.Sportowiec;

public class DaneWejsciowe {

    private final Osrodek osrodek;

    private final Sportowiec[] sportowcy;

    public DaneWejsciowe(Wezel[] wezly, Trasa[] trasy, Wyciag[] wyciagi, GrupaSportowcow[] grupySportowcow) {
        for (final Wezel wezel : wezly) {
            wezel.wychodzaceTrasy(znajdzWychodzaceTrasy(trasy, wezel));
            wezel.wychodzaceWyciagi(znajdzWychodzaceWyciagi(wyciagi, wezel));
        }
        this.osrodek = new Osrodek(wezly, trasy, wyciagi);
        this.sportowcy = przetworzGrupy(grupySportowcow);
    }

    public Osrodek osrodek() {
        return osrodek;
    }

    public Sportowiec[] sportowcy() {
        return sportowcy;
    }

    private Trasa[] znajdzWychodzaceTrasy(Trasa[] trasy, Wezel wezel) {
        int liczbaWychodzacychTras = 0;

        for (Trasa trasa : trasy) {
            if (trasa.poczatek().equals(wezel)) {
                liczbaWychodzacychTras++;
            }
        }

        Trasa[] wychodzaceTrasy = new Trasa[liczbaWychodzacychTras];
        int i = 0;

        for (Trasa trasa : trasy) {
            if (trasa.poczatek().equals(wezel)) {
                wychodzaceTrasy[i++] = trasa;
            }
        }

        return wychodzaceTrasy;
    }

    private Wyciag[] znajdzWychodzaceWyciagi(Wyciag[] wyciagi, Wezel wezel) {
        int liczbaWychodzacychWyciagow = 0;

        for (Wyciag wyciag : wyciagi) {
            if (wyciag.poczatek().equals(wezel)) {
                liczbaWychodzacychWyciagow++;
            }
        }

        Wyciag[] wychodzaceWyciagi = new Wyciag[liczbaWychodzacychWyciagow];
        int i = 0;

        for (Wyciag wyciag : wyciagi) {
            if (wyciag.poczatek().equals(wezel)) {
                wychodzaceWyciagi[i++] = wyciag;
            }
        }

        return wychodzaceWyciagi;
    }

    private Sportowiec[] przetworzGrupy(GrupaSportowcow[] grupySportowcow) {
        int sumaSportowcow = 0;

        for (GrupaSportowcow grupa : grupySportowcow) {
            sumaSportowcow += grupa.krotnosc();
        }

        Sportowiec[] sportowcy = new Sportowiec[sumaSportowcow];

        int obecneId = 0;

        for (GrupaSportowcow grupa : grupySportowcow) {
            Sportowiec[] sportowcyWGrupie = grupa.podajSportowcow();

            for (Sportowiec sportowiec : sportowcyWGrupie) {
                sportowcy[obecneId] = sportowiec;
                obecneId++;
            }
        }

        return sportowcy;
    }

    @Override
    public String toString() {
        return "DaneWejsciowe [osrodek=" + osrodek + ", sportowcy=" + Arrays.toString(sportowcy) + "]";
    }
}
