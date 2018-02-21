package magic.model;

import magic.model.event.MagicEvent;

public abstract class MagicAmount {

    public abstract int getAmount(final MagicSource source, final MagicPlayer player);

    public int getAmount(final MagicEvent event) {
        return getAmount(event.getSource(), event.getPlayer());
    }

    public int getPositiveAmount(final MagicSource source, final MagicPlayer player) {
        if (this == MagicAmountFactory.NegXCost) {
            // Return a negative number for NegXCost
            return Math.min(getAmount(source, player), 0);
        } else {
            return Math.max(getAmount(source, player), 0);
        }
    }

    public int getPositiveAmount(final MagicEvent event) {
        if (this == MagicAmountFactory.NegXCost) {
            // Return a negative number for NegXCost
            return Math.min(getAmount(event), 0);
        } else {
            return Math.max(getAmount(event), 0);
        }
    }

    public boolean isConstant() {
        return false;
    }

    public static int countEachProduct(final MagicAmount amount, final MagicAmount eachCount, final MagicEvent event) {
        if (eachCount != MagicAmountFactory.One && amount == MagicAmountFactory.XCost) {
            return eachCount.getPositiveAmount(event);
        } else if (eachCount != MagicAmountFactory.One && amount == MagicAmountFactory.NegXCost) {
            return -eachCount.getPositiveAmount(event);
        } else {
            return amount.getPositiveAmount(event) * eachCount.getPositiveAmount(event);
        }
    }
}
