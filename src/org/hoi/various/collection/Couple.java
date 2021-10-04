package org.hoi.various.collection;

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
