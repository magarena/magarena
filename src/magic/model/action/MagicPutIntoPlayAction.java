package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.MagicPayedCost;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public abstract class MagicPutIntoPlayAction extends MagicAction {

    private MagicPermanent permanent = MagicPermanent.NONE;
    private MagicPermanent enchantedPermanent = MagicPermanent.NONE;
    private MagicPayedCost payedCost = MagicPayedCost.NO_COST;
    private boolean validEnchanted = false;

    @Override
    public void doAction(final MagicGame game) {
        permanent=createPermanent(game);
        permanent.getFirstController().addPermanent(permanent);
        game.update();

        final int score=ArtificialScoringSystem.getTurnScore(game)-permanent.getStaticScore();

        validEnchanted = enchantedPermanent.isValid();
        if (validEnchanted) {
            enchantedPermanent.addAura(permanent);
            permanent.setEnchantedPermanent(enchantedPermanent);
        }

        game.addStatics(permanent);
        game.update();

        final MagicPlayer controller = permanent.getController();

        //execute come into play triggers
        for (final MagicTrigger<MagicPayedCost> trigger : permanent.getComeIntoPlayTriggers()) {
            game.executeTrigger(trigger,permanent,permanent,payedCost);
        }

        //execute other come into player triggers
        game.executeTrigger(MagicTriggerType.WhenOtherComesIntoPlay,permanent);

        setScore(controller,permanent.getScore()+permanent.getStaticScore()+score);

        game.checkUniquenessRule(permanent);
        game.setStateCheckRequired();
    }

    @Override
    public void undoAction(final MagicGame game) {
        // for change of control Auras, enchantedPermanent.isValid is false as
        // change of control action is undone. Thus we store the validity in
        // variable validEnchanted during doAction.
        if (validEnchanted) {
            enchantedPermanent.removeAura(permanent);
            permanent.setEnchantedPermanent(MagicPermanent.NONE);
        }
        permanent.getFirstController().removePermanent(permanent);
        game.removeTriggers(permanent);
        game.removeAllStatics(permanent);
    }

    void setEnchantedPermanent(final MagicPermanent aEnchantedPermanent) {
        enchantedPermanent = aEnchantedPermanent;
    }

    void setPayedCost(final MagicPayedCost aPayedCost) {
        payedCost = aPayedCost;
    }

    protected abstract MagicPermanent createPermanent(final MagicGame game);

    public MagicPermanent getPermanent() {
        return permanent;
    }

    @Override
    public String toString() {
        if (enchantedPermanent.isValid()) {
            return getClass().getSimpleName()+" ("+permanent+','+enchantedPermanent+')';
        } else {
            return getClass().getSimpleName()+" ("+permanent+')';
        }
    }
}
