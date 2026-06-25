package sportowcy.historia_przejazdow;

import osrodek.krawedz.Krawedz;
import osrodek.krawedz.Trasa;

import java.util.*;

public class HistoriaPrzejazdowSportowca {

    // RejestrHistorii przechowuje indeksy przejazdów daną krawędzia.
    private final Map<Krawedz, RejestrHistorii> historia;
    private int licznikPrzejazdow;

    public HistoriaPrzejazdowSportowca() {
        historia = new HashMap<>();
        licznikPrzejazdow = 0;
    }

    /**
     * Zwraca rejestr przejazdów dla danej krawędzi.
     * Jeśli rejestr nie jest jeszcze zmapowany, to zwraca pusty rejestr.
     */
    private RejestrHistorii pobierzRejestr(Krawedz krawedz) {
        return historia.getOrDefault(krawedz, new RejestrHistorii());
    }

    public int liczbaPrzejazdowKrawedzia(Krawedz krawedz) {
        return pobierzRejestr(krawedz).rozmiar();
    }

    /**
     * Zwraca indeks ostatniego zarejestrowanego zjazdu
     * lub 0, jeśli taki jescze nie nastąpił.
     */
    public int indeksOstatniegoZjazduTrasa(Trasa trasa) {
        return pobierzRejestr(trasa).indeksOstatniegoWpisu();
    }

    public int licznikPrzejazdow() {
        return licznikPrzejazdow;
    }

    /**
     * Funkcja odpowiedzialna za rejestrowanie historii przejazdów zgłaszanych przez inne Klasy.
     */
    public void obslozPrzejazd(Krawedz krawedz) {
        licznikPrzejazdow++;

        historia.putIfAbsent(krawedz, new RejestrHistorii());
        final RejestrHistorii rejestr = historia.get(krawedz);
        assert rejestr != null : "Blad pobrania wartosci z setu.";

        rejestr.dodaj(licznikPrzejazdow);
    }

    /**
     * Zwraca zapis rejestru przejazdów jako String.
     */
    public String pobierzZapisRejestru(Krawedz krawedz) {
        return pobierzRejestr(krawedz).toString();
    }

}
