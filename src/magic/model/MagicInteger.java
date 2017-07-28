package magic.model;

public class MagicInteger implements MagicCopyable {
    public final int value;

    public MagicInteger(final int v) {
        value = v;
    }

    @Override
    public MagicCopyable copy(final MagicCopyMap copyMap) {
        return this;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
