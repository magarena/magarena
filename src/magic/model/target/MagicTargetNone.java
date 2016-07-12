package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicCopyMap;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicObjectImpl;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;

public class MagicTargetNone extends MagicObjectImpl implements MagicTarget, MagicMappable<MagicTargetNone> {

    private static final MagicTarget INSTANCE=new MagicTargetNone();

    private MagicTargetNone() {}

    @Override
    public MagicTargetNone map(final MagicGame game) {
        return this;
    }

    @Override
    public MagicTarget copy(final MagicCopyMap copyMap) {
        return this;
    }

    @Override
    public void setPreventDamage(final int amount) {

    }

    @Override
    public boolean isValidTarget(final MagicSource source) {
        return false;
    }

    @Override
    public boolean isLegalTarget(final MagicPlayer player, final MagicTargetFilter<? extends MagicTarget> targetFilter) {
        return false;
    }

    @Override
    public boolean isSpell() {
        return false;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isPermanent() {
        return false;
    }

    @Override
    public boolean hasAbility(final MagicAbility ability) {
        return false;
    }

    @Override
    public boolean hasType(final MagicType type) {
        return false;
    }

    @Override
    public boolean hasSubType(final MagicSubType subType) {
        return false;
    }

    @Override
    public boolean hasColor(final MagicColor color) {
        return false;
    }

    @Override
    public int getColorFlags() {
        return 0;
    }

    @Override
    public int getPreventDamage() {
        return 0;
    }

    @Override
    public String getName() {
        return "no legal targets";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public MagicPlayer getController() {
        throw new RuntimeException("MagicTargetNone has no controller");
    }

    @Override
    public MagicCardDefinition getCardDefinition() {
        throw new RuntimeException("MagicTargetNone has no card definition");
    }

    public static final MagicTarget getInstance() {
        return INSTANCE;
    }

    public long getId() {
        return hashCode();
    }

    public long getStateId() {
        return hashCode();
    }

    @Override
    public int getCounters(final MagicCounterType counterType) {
        return 0;
    }

    @Override
    public void changeCounters(final MagicCounterType counterType,final int amount) {
        throw new RuntimeException(counterType + " cannot be modified on item on stack");
    }
}
