import dziennik.DziennikStandardoweWyjscie;
import kadra.mapki.pliki.WyjatekSystemuPlikow;
import kolejkaZdarzen.KolejkaPriorytetowaZdarzen;
import losowosc.DeterministycznaMaszynaLosujaca;
import symulacja.KreatorMapek;
import symulacja.Symulacja;
import wczytywacz.DaneWejsciowe;
import wczytywacz.Wczytywacz;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws WyjatekSystemuPlikow {
        assert args.length > 0 : "Brak ścieżki do katalogu w argumencie programu.";
        try {
            final DaneWejsciowe daneWejsciowe = wczytajWejscie();

            new Symulacja().przeprowadzSymulacje(new DziennikStandardoweWyjscie(),
                    new KolejkaPriorytetowaZdarzen(),
                    daneWejsciowe.osrodek(),
                    daneWejsciowe.sportowcy());

            final KreatorMapek kreatorMapek = new KreatorMapek(args[0], daneWejsciowe);
            kreatorMapek.generujWszystkie();
        } catch (WyjatekSystemuPlikow e) {
            System.err.println("Wystąpił problem z systemem plików (nie można utworzyć/zapisać pliku z mapką, itd)");
            System.err.println("Upewnij się że podana ścieżka jest poprawna i masz uprawnienia do zapisu w tej lokalizacji.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Wystąpił krytyczny błąd w programie. Proszę zglosic ten błąd deweloperowi programu.");
            e.printStackTrace();
        }
    }

    private static DaneWejsciowe wczytajWejscie() {
        Scanner scanner = new Scanner(System.in);

        // Ustawiamy region na angielski żeby Scanner parsował liczby
        // zmiennoprzecinkowe z '.' zamiast ','.
        scanner.useLocale(Locale.ENGLISH);

        Wczytywacz wczytywacz = new Wczytywacz(scanner, new DeterministycznaMaszynaLosujaca(0));
        return wczytywacz.wczytajWejscie();
    }

    // TODO oddzielna klasa na statystyki danego wyciagu, trasy, itp
    // TODO wyluskac comparatory tam gdzie sie da
    // TODO rekord do trzymania alfa beta itp w sportowcu?
    // TODO asercje!
}
