package magic.model.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class PlayCardFromStackAction extends MagicAction {

    private final MagicCardOnStack cardOnStack;
    private final MagicCardDefinition cardDef;

    private MagicPermanent permanent = MagicPermanent.NONE;
    private MagicPermanent enchantedPermanent = MagicPermanent.NONE;
    private MagicPayedCost payedCost = MagicPayedCost.NO_COST;
    private boolean validEnchanted = false;
    private List<? extends MagicPermanentAction> modifications = Collections.<MagicPermanentAction>emptyList();

    public PlayCardFromStackAction(final MagicCardOnStack aCardOnStack, final MagicCardDefinition aCardDef, final List<? extends MagicPermanentAction> aModifications) {
        cardOnStack = aCardOnStack;
        cardDef = aCardDef;
        payedCost = aCardOnStack.getPayedCost();
        modifications = aModifications;
    }

    public PlayCardFromStackAction(final MagicCardOnStack aCardOnStack, final MagicCardDefinition aCardDef, final MagicPermanentAction... aModifications) {
        this(aCardOnStack, aCardDef, Arrays.asList(aModifications));
    }

    public PlayCardFromStackAction(final MagicCardOnStack aCardOnStack, final MagicPermanentAction... aModifications) {
        this(aCardOnStack, aCardOnStack.getCardDefinition(), aModifications);
    }

    public PlayCardFromStackAction(final MagicCardOnStack cardOnStack, final MagicPermanent aEnchantedPermanent, final MagicPermanentAction... aModifications) {
        this(cardOnStack, aModifications);
        enchantedPermanent = aEnchantedPermanent;
    }

    protected MagicPermanent createPermanent(final MagicGame game) {
        cardOnStack.setMoveLocation(MagicLocationType.Battlefield);
        return game.createPermanent(cardOnStack.getCard(),cardDef,cardOnStack.getController());
    }

    @Override
    public void doAction(final MagicGame game) {
        permanent=createPermanent(game);
        permanent.getFirstController().addPermanent(permanent);

        if (cardOnStack.isCast() && cardOnStack.getFromLocation() == MagicLocationType.OwnersHand) {
            game.doAction(ChangeStateAction.Set(permanent, MagicPermanentState.CastFromHand));
        }

        //comes into play with/as, such as manifest
        for (final MagicPermanentAction action : cardOnStack.getModifications()) {
            action.doAction(permanent);
        }
        //comes into play with/as, such as bestowed
        for (final MagicPermanentAction action : modifications) {
            action.doAction(permanent);
        }

        game.update();

        final int score=ArtificialScoringSystem.getTurnScore(game)-permanent.getStaticScore();

        validEnchanted = enchantedPermanent.isValid();
        if (validEnchanted) {
            enchantedPermanent.addAura(permanent);
            permanent.setEnchantedPermanent(enchantedPermanent);
        }

        //execute comes into play with
        for (final MagicTrigger<MagicPayedCost> trigger : permanent.getComeIntoPlayTriggers()) {
            if (trigger.getPriority() == MagicTrigger.REPLACEMENT) {
                game.executeTrigger(trigger,permanent,permanent,payedCost);
            }
        }

        game.addStatics(permanent);
        game.update();

        //execute come into play triggers
        for (final MagicTrigger<MagicPayedCost> trigger : permanent.getComeIntoPlayTriggers()) {
            if (trigger.getPriority() > MagicTrigger.REPLACEMENT) {
                game.executeTrigger(trigger,permanent,permanent,payedCost);
            }
        }

        //execute other come into player triggers
        game.executeTrigger(MagicTriggerType.WhenOtherComesIntoPlay,permanent);

        setScore(permanent.getController(),permanent.getScore()+permanent.getStaticScore()+score);

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

    @Override
    public String toString() {
        if (enchantedPermanent.isValid()) {
            return getClass().getSimpleName()+" ("+permanent+','+enchantedPermanent+')';
        } else {
            return getClass().getSimpleName()+" ("+permanent+')';
        }
    }
}
