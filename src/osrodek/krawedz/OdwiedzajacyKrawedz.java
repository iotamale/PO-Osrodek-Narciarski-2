package osrodek.krawedz;

import osrodek.krawedz.wyciag.Wyciag;

/**
 * Interfejs implementujący wzorzec projektowy odwiedzajacego.
 */
public interface OdwiedzajacyKrawedz<T> {
    T odwiedz(Trasa trasa);
    T odwiedz(Wyciag wyciag);
}
