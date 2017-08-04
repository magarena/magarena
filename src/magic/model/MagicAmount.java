package magic.model;

import magic.model.event.MagicEvent;

public abstract class MagicAmount {

    public abstract int getAmount(final MagicSource source, final MagicPlayer player);

    public int getAmount(final MagicEvent event) {
        return getAmount(event.getSource(), event.getPlayer());
    }

    public boolean isConstant() {
        return false;
    }

    public static int countEachProduct(final MagicAmount amount, final MagicAmount eachCount, final MagicEvent event) {
        if (eachCount != MagicAmountFactory.One && amount == MagicAmountFactory.XCost) {
            return eachCount.getAmount(event);
        } else if (eachCount != MagicAmountFactory.One && amount == MagicAmountFactory.NegXCost) {
            return -eachCount.getAmount(event);
        } else {
            return amount.getAmount(event) * eachCount.getAmount(event);
        }
    }
}
