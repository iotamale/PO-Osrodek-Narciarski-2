package symulacja;

import kadra.mapki.styl.StylKrawedzi;

/**
 * Interfejs pozwalający na dodawanie krawędzi do generatora.
 * Stosujemy go, żeby nie duplikować kodu w klasie KreatorMapek
 * (a dokładnie funkcji void generujMapke()) dla wersji przyjmującej
 * List<String> oraz String.
 */
interface KreatorKrawedzi<T> {

    void dodaj(int nrPoczatek, int nrKoniec, StylKrawedzi styl, T opis);

}
