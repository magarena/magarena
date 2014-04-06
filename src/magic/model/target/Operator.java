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
    EQUAL() {
        public boolean cmp(final int v1, final int v2) {
            return v1 == v2;
        }
    };
    public abstract boolean cmp(final int v1, final int v2);
}
