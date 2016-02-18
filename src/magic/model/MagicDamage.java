package magic.model;

import magic.model.target.MagicTarget;

public class MagicDamage {

    public static final boolean Combat = true;
    public static final boolean Any = false;

    private final MagicSource source;
    private MagicTarget target;
    private int amount;
    private int dealtAmount;
    private final boolean combat;
    private boolean unpreventable;
    private boolean noRegeneration;

    private MagicDamage(final MagicSource source,final MagicTarget target,final int amount,final boolean combat) {
        this.source=source;
        this.target=target;
        this.amount=amount;
        this.combat=combat;
    }

    public MagicDamage(final MagicSource source,final MagicTarget target,final int amount) {
        this(source, target, amount, false);
    }

    public static final MagicDamage Combat(final MagicSource source,final MagicTarget target,final int amount) {
        return new MagicDamage(source, target, amount, true);
    }

    public MagicSource getSource() {
        return source;
    }

    public boolean isSource(final MagicSource other) {
        return source == other;
    }

    public boolean isSourcePermanent() {
        return source.isPermanent();
    }

    public void setTarget(final MagicTarget target) {
        this.target=target;
    }

    public MagicTarget getTarget() {
        return target;
    }

    public boolean isTarget(final MagicTarget other) {
        return target == other;
    }

    public boolean isTargetPlayer() {
        return target.isPlayer();
    }

    public boolean isTargetCreature() {
        return target.isCreaturePermanent();
    }

    public MagicPlayer getTargetPlayer() {
        return (MagicPlayer)target;
    }

    public MagicPermanent getTargetPermanent() {
        return (MagicPermanent)target;
    }

    public MagicPermanent getSourcePermanent() {
        return (MagicPermanent)source;
    }

    public void setAmount(final int amt) {
        amount = amt;
    }

    public int replace() {
        final int oldAmount = amount;
        amount = 0;
        return oldAmount;
    }

    public int prevent() {
        return prevent(amount);
    }

    public int prevent(final int amt) {
        final int oldAmount = amount;
        if (!unpreventable) {
            amount -= amt;
        }
        return oldAmount - amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setDealtAmount(final int dealtAmount) {
        this.dealtAmount=dealtAmount;
    }

    public int getDealtAmount() {
        return dealtAmount;
    }

    public boolean isCombat() {
        return combat;
    }

    public void setUnpreventable() {
        unpreventable=true;
    }

    public boolean isPreventable() {
        return !unpreventable;
    }

    public boolean isUnpreventable() {
        return unpreventable;
    }

    public void setNoRegeneration() {
        noRegeneration=true;
    }

    public boolean hasNoRegeneration() {
        return noRegeneration;
    }
}
