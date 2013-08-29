package magic.model;

public interface MagicAbilityStore {
    void add(final MagicChangeCardDefinition change);
    void addAbility(final MagicAbility ability);
}
