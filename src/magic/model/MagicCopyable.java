package magic.model;

public interface MagicCopyable {
    MagicCopyable copy(final MagicCopyMap copyMap);
    long getStateId();
}
