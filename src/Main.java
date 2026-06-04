import java.util.Locale;
import java.util.Scanner;

import dziennik.DziennikStandardoweWyjscie;
import kolejkaZdarzen.ProstaTablicaZdarzen;
import losowosc.DeterministycznaMaszynaLosujaca;
import symulacja.Symulacja;
import wczytywacz.DaneWejsciowe;
import wczytywacz.Wczytywacz;

public class Main {

    public static void main(String[] args) {
        DaneWejsciowe daneWejsciowe = wczytajWejscie();

        new Symulacja().przeprowadzSymulacje(new DziennikStandardoweWyjscie(),
            new ProstaTablicaZdarzen(),
            daneWejsciowe.osrodek(),
            daneWejsciowe.sportowcy());
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
