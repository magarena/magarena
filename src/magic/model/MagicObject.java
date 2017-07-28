package magic.model;

public interface MagicObject extends MagicCopyable {
    boolean     isToken();
    boolean     isSpell();
    boolean     isSpell(MagicType type);
    boolean     isSpell(MagicSubType subType);
    boolean     isInstantOrSorcerySpell();
    boolean     isPlayer();
    boolean     isPermanent();
    boolean     isCreaturePermanent();
    boolean     isPlaneswalkerPermanent();
    boolean     hasColor(final MagicColor color);
    int         getColorFlags();
    boolean     shareColor(final MagicObject other);
    boolean     hasAbility(final MagicAbility ability);
    boolean     hasType(final MagicType type);
    boolean     hasSubType(final MagicSubType subType);
    boolean     hasCounters(final MagicCounterType counterType);
    int         getCounters(final MagicCounterType counterType);
    void        changeCounters(final MagicCounterType counterType,final int amount);
    String      getName();
    MagicPlayer getController();
    MagicPlayer getOpponent();
    boolean     isFriend(final MagicObject other);
    boolean     isEnemy(final MagicObject other);
    long        getId();
    long        getStateId();
    MagicCardDefinition getCardDefinition();
}
