package kolejkaZdarzen;

import java.util.Comparator;

class ComparatorParZdarzen implements Comparator<ParaZdarzeniowa> {

    @Override
    public int compare(ParaZdarzeniowa o1, ParaZdarzeniowa o2) {
        final int porownanieCzasu = o1.zdarzenie().moment().compareTo(o2.zdarzenie().moment());

        if (porownanieCzasu != 0) {
            return porownanieCzasu;
        }

        return o1.porownajNumery(o2);
    }

}
