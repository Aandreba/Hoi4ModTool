package org.hoi.various.collection.tuple;

public class Couple<A,B> {
    protected A first;
    protected B last;

    public Couple (A first, B last) {
        this.first = first;
        this.last = last;
    }

    public A getFirst() {
        return first;
    }

    public B getLast() {
        return last;
    }

    public boolean isEmpty () {
        return first == null && last == null;
    }

    // STATIC
    public static <A,B> Couple<A,B> empty () {
        return new Couple<>(null, null);
    }

    public static class Writeable<A,B> extends Couple<A,B> {
        public Writeable(A first, B last) {
            super (first, last);
        }

        public void setFirst (A value) {
            first = value;
        }

        public void setLast (B value) {
            last = value;
        }
    }
}
