package magic.model.target;

public enum Operator {
    LESS_THAN() {
        public boolean cmp(final int v1, final int v2) {
            return v1 < v2;
        }
    },
    LESS_THAN_OR_EQUAL() {
        public boolean cmp(final int v1, final int v2) {
            return v1 <= v2;
        }
    },
    GREATER_THAN() {
        public boolean cmp(final int v1, final int v2) {
            return v1 > v2;
        }
    },
    GREATER_THAN_OR_EQUAL() {
        public boolean cmp(final int v1, final int v2) {
            return v1 >= v2;
        }
    },
    EQUAL() {
        public boolean cmp(final int v1, final int v2) {
            return v1 == v2;
        }
    },
    ANY() {
        public boolean cmp(final int v1, final int v2) {
            return true;
        }
    };
    public abstract boolean cmp(final int v1, final int v2);
}
