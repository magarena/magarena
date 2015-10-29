package magic.model;

public interface MagicObject extends MagicCopyable {
    boolean     isSpell();
    boolean     isPlayer();
    boolean     isPermanent();
    boolean     hasColor(final MagicColor color);
    boolean     hasAbility(final MagicAbility ability);
    boolean     hasType(final MagicType type);
    boolean     hasSubType(final MagicSubType subType);
    
    int         getCounters(final MagicCounterType counterType);
    void        changeCounters(final MagicCounterType counterType,final int amount);
    String      getName();
    MagicPlayer getController();
    long        getStateId();
    long        getId();
    MagicCardDefinition getCardDefinition();
    
    default boolean isToken() {
        return isPermanent() && ((MagicPermanent)this).isToken();
    }
    default boolean isSpell(MagicType type) {
        return isSpell() && hasType(type);
    }
    default boolean isSpell(MagicSubType subType) {
        return isSpell() && hasSubType(subType);
    }
    default boolean isInstantOrSorcerySpell() {
        return isSpell(MagicType.Instant) || isSpell(MagicType.Sorcery);
    }
    default boolean isCreature() {
        return isPermanent() && hasType(MagicType.Creature);
    }
    default boolean isPlaneswalker() {
        return isPermanent() && hasType(MagicType.Planeswalker);
    }
    default boolean hasCounters(final MagicCounterType counterType) {
        return getCounters(counterType) > 0; 
    }
    default MagicPlayer getOpponent() {
        return getController().getOpponent();
    }
    default boolean isFriend(final MagicObject other) {
        return getController() == other.getController();
    }
    default boolean isEnemy(final MagicObject other) {
        return getOpponent() == other.getController();
    }
    
    public static long getStateId(final Object obj) {
        if (obj == null) {
            return -1L;
        } else if (obj instanceof MagicPlayer) {
            return ((MagicPlayer)obj).getId();
        } else if (obj instanceof MagicObject) {
            return ((MagicObject)obj).getStateId();
        } else if (obj instanceof MagicMappable) {
            return ((MagicMappable)obj).getId();
        } else {
            return obj.hashCode();
        }
    }
}
