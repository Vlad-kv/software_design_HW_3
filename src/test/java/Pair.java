public class Pair <T1, T2> {
    T1 value1;
    T2 value2;

    public Pair(T1 value1, T2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public boolean equals(Object object) {
        if (! (object instanceof Pair)) {
            return false;
        }
        Pair <T1, T2> p = (Pair<T1, T2>) object;
        return (p.value1.equals(value1) && p.value2.equals(value2));
    }
}
