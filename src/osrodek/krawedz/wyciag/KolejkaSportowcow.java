package osrodek.krawedz.wyciag;

import sportowcy.Sportowiec;

/**
 * Reprezentuje kolejkę prostą sportowców do wyciągu.
 */
public interface KolejkaSportowcow {

    void dodaj(Sportowiec sportowiec);

    Sportowiec[] zdejmij(int ile);

    int rozmiar();
}
