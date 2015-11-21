package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.AtUpkeepTrigger;

import java.util.Set;

public class DetainAction extends MagicAction {

    private final MagicPermanent targetPermanent;
    private final MagicPlayer sourceController;

    private static final MagicStatic Detain = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            flags.add(MagicAbility.CannotAttackOrBlock);
            flags.add(MagicAbility.CantActivateAbilities);
        }
    };

    private final AtUpkeepTrigger Cleanup = new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            if (upkeepPlayer.getId() == sourceController.getId()) {
                game.addDelayedAction(new RemoveStaticAction(permanent, Detain));
                game.addDelayedAction(new RemoveTriggerAction(permanent, this));
            }
            return MagicEvent.NONE;
        }
    };

    public DetainAction(final MagicPlayer aSourceController, final MagicPermanent aTargetPermanent) {
        sourceController = aSourceController;
        targetPermanent = aTargetPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new AddStaticAction(targetPermanent, Detain));
        game.doAction(new AddTriggerAction(targetPermanent, Cleanup));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}
