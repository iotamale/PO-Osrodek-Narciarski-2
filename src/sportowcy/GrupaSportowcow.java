package sportowcy;

import czas.Interwal;
import java.util.ArrayList;
import java.util.List;

public class GrupaSportowcow {

    private final Sportowiec schematSportowca;

    private final int krotnosc;

    private final Interwal odstepMiedzySportowcami;

    public Sportowiec schematSportowca() {
        return schematSportowca;
    }

    public int krotnosc() {
        return krotnosc;
    }

    public Interwal odstepMiedzySportowcami() {
        return odstepMiedzySportowcami;
    }

    public GrupaSportowcow(Sportowiec schematSportowca, int krotnosc, Interwal odstepMiedzySportowcami) {
        assert krotnosc > 0 : "Ilość sportowców w grupie musi być dodatnia";

        this.schematSportowca = schematSportowca;
        this.krotnosc = krotnosc;
        this.odstepMiedzySportowcami = odstepMiedzySportowcami;
    }

    public List<Sportowiec> podajSportowcow() {
        List<Sportowiec> sportowcy = new ArrayList<>(krotnosc);
        sportowcy.add(schematSportowca);

        Sportowiec poprzedni = schematSportowca;
        for (int i = 1; i < krotnosc; i++) {
            Sportowiec nowy = poprzedni.kopia(1, odstepMiedzySportowcami);
            sportowcy.add(nowy);
            poprzedni = nowy;
        }

        return sportowcy;
    }

    @Override
    public String toString() {
        return "GrupaSportowcow [schematSportowca=" + schematSportowca + ", krotnosc=" + krotnosc
            + ", odstepMiedzySportowcami=" + odstepMiedzySportowcami + "]";
    }
}
