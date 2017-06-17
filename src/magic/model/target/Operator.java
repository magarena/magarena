package magic.model.target;

public enum Operator {
    LESS_THAN() {
        @Override
        public boolean cmp(final int v1, final int v2) {
            return v1 < v2;
        }
    },
    LESS_THAN_OR_EQUAL() {
        @Override
        public boolean cmp(final int v1, final int v2) {
            return v1 <= v2;
        }
    },
    GREATER_THAN() {
        @Override
        public boolean cmp(final int v1, final int v2) {
            return v1 > v2;
        }
    },
    GREATER_THAN_OR_EQUAL() {
        @Override
        public boolean cmp(final int v1, final int v2) {
            return v1 >= v2;
        }
    },
    EQUAL() {
        @Override
        public boolean cmp(final int v1, final int v2) {
            return v1 == v2;
        }
    },
    ANY() {
        @Override
        public boolean cmp(final int v1, final int v2) {
            return true;
        }
    };
    public abstract boolean cmp(final int v1, final int v2);
}
