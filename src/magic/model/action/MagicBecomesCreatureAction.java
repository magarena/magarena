package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicStatic;

public class MagicBecomesCreatureAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicStatic[] mstatics;

    public MagicBecomesCreatureAction(final MagicPermanent aPermanent,final MagicStatic... aMstatics) {
        permanent = aPermanent;
        mstatics = aMstatics;
    }

    @Override
    public void doAction(final MagicGame game) {
        for (final MagicStatic mstatic : mstatics) {
            game.doAction(new AddStaticAction(permanent, mstatic));
        }
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {}
}
