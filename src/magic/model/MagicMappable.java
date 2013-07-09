package magic.model;

public interface MagicMappable<T> {
    T map(final MagicGame game);
    long getId();
}
