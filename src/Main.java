import java.util.Locale;
import java.util.Scanner;

import dziennik.DziennikStandardoweWyjscie;
import kadra.mapki.GeneratorMapek;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kadra.mapki.styl.GruboscKonturu;
import kadra.mapki.styl.StylKrawedzi;
import kadra.mapki.styl.StylLinii;
import kadra.mapki.styl.StylWezla;
import kolejkaZdarzen.KolejkaPriorytetowaZdarzen;
import losowosc.DeterministycznaMaszynaLosujaca;
import osrodek.Wezel;
import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import symulacja.KreatorMapek;
import symulacja.Symulacja;
import wczytywacz.DaneWejsciowe;
import wczytywacz.Wczytywacz;

public class Main {

    public static void main(String[] args) throws WyjatekSystemuPlikow {
        assert args.length > 0 : "Brak ścieżki do katalogu w argumencie programu.";
        final DaneWejsciowe daneWejsciowe = wczytajWejscie();

        new Symulacja().przeprowadzSymulacje(new DziennikStandardoweWyjscie(),
            new KolejkaPriorytetowaZdarzen(),
            daneWejsciowe.osrodek(),
            daneWejsciowe.sportowcy());

        final KreatorMapek kreatorMapek = new KreatorMapek(args[0], daneWejsciowe);
        kreatorMapek.generujMapkeParametrow();
    }

    private static DaneWejsciowe wczytajWejscie() {
        Scanner scanner = new Scanner(System.in);

        // Ustawiamy region na angielski żeby Scanner parsował liczby
        // zmiennoprzecinkowe z '.' zamiast ','.
        scanner.useLocale(Locale.ENGLISH);

        Wczytywacz wczytywacz = new Wczytywacz(scanner, new DeterministycznaMaszynaLosujaca(0));
        return wczytywacz.wczytajWejscie();
    }
}
