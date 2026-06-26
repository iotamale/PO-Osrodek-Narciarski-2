package wczytywacz;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import czas.Interwal;
import czas.Moment;
import losowosc.MaszynaLosujaca;
import osrodek.Wezel;
import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;
import sportowcy.*;
import sportowcy.sportowcy_planujacy.SportowiecKolekcjoner;
import sportowcy.SportowiecLokalny;
import sportowcy.sportowcy_planujacy.SportowiecZachlanny;

public class Wczytywacz {

    private static final String OZNACZENIE_LOKALNY = "L";
    private static final String OZNACZENIE_ZACHLANNY = "Z";
    private static final String OZNACZENIE_KOLEKCJONER = "K";

    private final Scanner scanner;
    private final MaszynaLosujaca maszynaLosujaca;

    public Wczytywacz(Scanner scanner, MaszynaLosujaca maszynaLosujaca) {
        this.scanner = scanner;
        this.maszynaLosujaca = maszynaLosujaca;
    }

    public DaneWejsciowe wczytajWejscie() {
        List<Wezel> wezly = wczytajWezly();
        List<Wyciag> wyciagi = wczytajWyciagi(wezly);
        List<Trasa> trasy = wczytajTrasy(wezly);
        List<GrupaSportowcow> grupySportowcow = wczytajGrupySportowcow(wezly);
        return new DaneWejsciowe(wezly, trasy, wyciagi, grupySportowcow);
    }

    private List<Wezel> wczytajWezly() {
        int liczbaWezlow = scanner.nextInt();

        List<Wezel> wezly = new ArrayList<>(liczbaWezlow);

        for (int id = 0; id < liczbaWezlow; id++) {
            int wysokosc = scanner.nextInt();
            int wspolrzednaX = scanner.nextInt();
            int wspolrzednaY = scanner.nextInt();
            boolean czyStartowy = scanner.findInLine("s") != null;
            wezly.add(new Wezel(id, wysokosc, wspolrzednaX, wspolrzednaY, czyStartowy));
        }

        return wezly;
    }

    private List<Wyciag> wczytajWyciagi(List<Wezel> wezly) {
        int liczbaWyciagow = scanner.nextInt();

        List<Wyciag> wyciagi = new ArrayList<>(liczbaWyciagow);

        for (int id = 0; id < liczbaWyciagow; id++) {
            int poczatek = scanner.nextInt();
            int koniec = scanner.nextInt();
            int odstep = scanner.nextInt();
            int maksymalnaWielkoscGrupy = scanner.nextInt();
            int czasPrzejazdu = scanner.nextInt();

            Wyciag wyciag = new Wyciag(id,
                wezly.get(poczatek),
                wezly.get(koniec),
                new Interwal(odstep),
                new Interwal(czasPrzejazdu),
                maksymalnaWielkoscGrupy);

            wyciagi.add(wyciag);
        }

        return wyciagi;
    }

    private List<Trasa> wczytajTrasy(List<Wezel> wezly) {
        int liczbaTras = scanner.nextInt();

        List<Trasa> trasy = new ArrayList<>(liczbaTras);

        for (int id = 0; id < liczbaTras; id++) {
            int poczatek = scanner.nextInt();
            int koniec = scanner.nextInt();
            int poziomTrudnosci = scanner.nextInt();
            int czasPrzejazdu = scanner.nextInt();
            Interwal dlugosc = new Interwal(czasPrzejazdu);
            double bazowaAtrakcyjnosc = scanner.nextDouble();
            double odpornoscNaNierownosci = scanner.nextDouble();

            Trasa trasa = new Trasa(id,
                wezly.get(poczatek),
                wezly.get(koniec),
                dlugosc,
                poziomTrudnosci,
                bazowaAtrakcyjnosc,
                odpornoscNaNierownosci);

            trasy.add(trasa);
        }

        return trasy;
    }

    private List<GrupaSportowcow> wczytajGrupySportowcow(List<Wezel> wezly) {
        int nastepneId = 0;
        int liczbaGrup = scanner.nextInt();

        List<GrupaSportowcow> grupySportowcow = new ArrayList<>(liczbaGrup);

        for (int grupa = 0; grupa < liczbaGrup; grupa++) {
            GrupaSportowcow nowaGrupa = wczytajGrupeSportowcow(nastepneId, wezly);
            grupySportowcow.add(nowaGrupa);
            nastepneId += nowaGrupa.krotnosc();
        }

        return grupySportowcow;
    }

    private Sportowiec stworzPierwszegoSportowca(String rodzaj, int id,
                                                 int poziom, double wspSpontanicznosci,
                                                 double wD, double wJN,
                                                 boolean sledzony, Wezel startowy,
                                                 Moment start, MaszynaLosujaca maszyna,
                                                 double wspZnudzenia, double wZ) {

        return switch (rodzaj) {
            case OZNACZENIE_LOKALNY -> new SportowiecLokalny(id, poziom, wspSpontanicznosci, wD,
                    wJN, sledzony, startowy, start, maszyna, wspZnudzenia, wZ);
            case OZNACZENIE_KOLEKCJONER -> new SportowiecKolekcjoner(id, poziom, wspSpontanicznosci, wD,
                    wJN, sledzony, startowy, start, maszyna, wspZnudzenia, wZ);
            case OZNACZENIE_ZACHLANNY -> new SportowiecZachlanny(id, poziom, wspSpontanicznosci, wD,
                    wJN, sledzony, startowy, start, maszyna, wspZnudzenia, wZ);
            default -> null;
        };

    }

    private GrupaSportowcow wczytajGrupeSportowcow(int nastepneId, List<Wezel> wezly) {
        int liczbaSportowcowWGrupie = scanner.nextInt();
        int poziomZaawansowania = scanner.nextInt();
        double wspolczynnikSpontanicznosci = scanner.nextDouble();
        double wspolczynnikZnudzenia = scanner.nextDouble();
        String oznaczenieRodzaju = scanner.next();
        boolean czySledzeni = scanner.findInLine("s") != null;

        double wagaDopasowania = scanner.nextDouble();
        double wagaJakosciNawierzchni = scanner.nextDouble();
        double wagaZnudzenia = scanner.nextDouble();
        int idPoczatkowegoWezla = scanner.nextInt();

        Moment start = wczytajMoment();

        Interwal odstepCzasowy = new Interwal(0);

        if (liczbaSportowcowWGrupie > 1) {
            odstepCzasowy = new Interwal(scanner.nextInt());
        }

        final Sportowiec pierwszySportowiec = stworzPierwszegoSportowca(oznaczenieRodzaju, nastepneId,
                poziomZaawansowania, wspolczynnikSpontanicznosci, wagaDopasowania, wagaJakosciNawierzchni, czySledzeni,
                wezly.get(idPoczatkowegoWezla), start, maszynaLosujaca, wspolczynnikZnudzenia, wagaZnudzenia);
        
        assert pierwszySportowiec != null : "Bledny identyifkator rodzaju sportowca.";

        return new GrupaSportowcow(pierwszySportowiec, liczbaSportowcowWGrupie, odstepCzasowy);
    }

    private Moment wczytajMoment() {
        String napis = scanner.next();
        String[] napisy = napis.split(":");
        int[] liczby = new int[napisy.length];

        for (int i = 0; i < liczby.length; i++) {
            liczby[i] = Integer.parseInt(napisy[i]);
        }

        return new Moment(liczby[0], liczby[1], liczby[2]);
    }
}
