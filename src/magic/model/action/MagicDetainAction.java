package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.event.MagicEvent;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicAddTriggerAction;
import magic.model.action.MagicRemoveStaticAction;
import magic.model.action.MagicRemoveTriggerAction;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

import java.util.Set;

public class MagicDetainAction extends MagicAction {

    private final MagicPermanent permanent;
    private final MagicPlayer sourceController;

    private static final MagicStatic Detain = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            flags.add(MagicAbility.CannotAttackOrBlock);
            flags.add(MagicAbility.CantActivateAbilities);
        }
    };
        
    private final MagicAtUpkeepTrigger Cleanup = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            if (upkeepPlayer.getId() == sourceController.getId()) {
                game.addDelayedAction(new MagicRemoveStaticAction(permanent, Detain));
                game.addDelayedAction(new MagicRemoveTriggerAction(permanent, this));
            }
            return MagicEvent.NONE;
        }
    };

    public MagicDetainAction(final MagicPlayer controller, final MagicPermanent aPermanent) {
        sourceController = controller;
        permanent = aPermanent;
    }

    @Override
    public void doAction(final MagicGame game) {
        game.doAction(new MagicAddStaticAction(permanent, Detain));
        game.doAction(new MagicAddTriggerAction(permanent, Cleanup));
    }

    @Override
    public void undoAction(final MagicGame game) {
    }
}
