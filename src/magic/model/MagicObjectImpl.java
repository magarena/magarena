package magic.model;

public abstract class MagicObjectImpl implements MagicObject {
    @Override
    public boolean isFriend(final MagicObject other) {
        return getController() == other.getController();
    }

    @Override
    public boolean isEnemy(final MagicObject other) {
        return getOpponent() == other.getController();
    }

    @Override
    public MagicPlayer getOpponent() {
        return getController().getOpponent();
    }

    @Override
    public boolean isCreaturePermanent() {
        return isPermanent() && hasType(MagicType.Creature);
    }

    @Override
    public boolean isPlaneswalkerPermanent() {
        return isPermanent() && hasType(MagicType.Planeswalker);
    }

    @Override
    public boolean isInstantOrSorcerySpell() {
        return isSpell(MagicType.Instant) || isSpell(MagicType.Sorcery);
    }

    @Override
    public boolean isSpell(MagicType type) {
        return isSpell() && hasType(type);
    }

    @Override
    public boolean isSpell(MagicSubType subType) {
        return isSpell() && hasSubType(subType);
    }

    @Override
    public boolean isToken() {
        return isPermanent() && ((MagicPermanent)this).isToken();
    }

    @Override
    public boolean hasCounters(final MagicCounterType counterType) {
        return getCounters(counterType) > 0;
    }

    @Override
    public boolean shareColor(final MagicObject other) {
        return (getColorFlags() & other.getColorFlags()) != 0;
    }

    public static long getStateId(final Object obj) {
        if (obj == null) {
            return -1L;
        } else if (obj instanceof MagicPlayer) {
            return ((MagicPlayer)obj).getId();
        } else if (obj instanceof MagicCopyable) {
            return ((MagicCopyable)obj).getStateId();
        } else if (obj instanceof MagicMappable) {
            return ((MagicMappable)obj).getId();
        } else {
            return obj.hashCode();
        }
    }
}
