package org.hoi.various.collection.tuple;

public class Triple<A,B,C> {
    protected A first;
    protected B mid;
    protected C last;

    public Triple(A first, B mid, C last) {
        this.first = first;
        this.mid = mid;
        this.last = last;
    }

    public A getFirst() {
        return first;
    }

    public B getMid() {
        return mid;
    }

    public C getLast() {
        return last;
    }

    public boolean isEmpty () {
        return first == null && mid == null && last == null;
    }

    // STATIC
    public static <A,B,C> Triple<A,B,C> empty () {
        return new Triple<>(null, null, null);
    }

    public static class Writeable<A,B,C> extends Triple<A,B,C> {
        public Writeable(A first, B mid, C last) {
            super(first, mid, last);
        }

        public void setFirst (A value) {
            this.first = value;
        }

        public void setMid (B value) {
            this.mid = value;
        }

        public void setLast (C value) {
            this.last = value;
        }
    }
}
