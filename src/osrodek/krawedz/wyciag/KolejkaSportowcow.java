package osrodek.krawedz.wyciag;

import sportowcy.Sportowiec;

import java.util.List;

/**
 * Reprezentuje kolejkę prostą sportowców do wyciągu.
 */
public interface KolejkaSportowcow {

    void dodaj(Sportowiec sportowiec);

    List<Sportowiec> zdejmij(int ile);

    int rozmiar();
}
