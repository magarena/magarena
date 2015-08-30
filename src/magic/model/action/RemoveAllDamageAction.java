package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;

public class RemoveAllDamageAction extends MagicAction {

    private final MagicPermanent permanent;
    private int oldDamage;

    RemoveAllDamageAction(final MagicPermanent permanent) {

        this.permanent=permanent;
    }

    @Override
    public void doAction(final MagicGame game) {

        oldDamage=permanent.getDamage();
        permanent.setDamage(0);
    }

    @Override
    public void undoAction(final MagicGame game) {

        permanent.setDamage(oldDamage);
    }
}
