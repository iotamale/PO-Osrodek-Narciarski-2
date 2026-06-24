package osrodek;

import java.util.List;

import osrodek.krawedz.Trasa;
import osrodek.krawedz.wyciag.Wyciag;

public class Osrodek {

    private final List<Wezel> wezly;

    private final List<Trasa> trasy;

    private final List<Wyciag> wyciagi;

    public Osrodek(List<Wezel> wezly, List<Trasa> trasy, List<Wyciag> wyciagi) {
        this.wezly = wezly;
        this.trasy = trasy;
        this.wyciagi = wyciagi;
    }

    public List<Wezel> wezly() {
        return wezly;
    }

    public List<Trasa> trasy() {
        return trasy;
    }

    public List<Wyciag> wyciagi() {
        return wyciagi;
    }

    @Override
    public String toString() {
        return "Osrodek [wezly=" + wezly + ", trasy=" + trasy + ", wyciagi="
            + wyciagi + "]";
    }
}
