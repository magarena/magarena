package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveStaticAction;
import magic.model.action.MagicAddStaticAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Roil_Elemental {

    public static final Object T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent played) {
            final MagicPlayer player = permanent.getController();
            return (player == played.getController() && played.isLand()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                        player + " may gain control of target creature for as long as you control " + permanent + ".",
                        MagicTargetChoice.NEG_TARGET_CREATURE),
                    MagicEvent.NO_DATA,
                    this,
                    "You may$ gain control of target creature$ for as long as you control " + permanent + ".") :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game, 
                final MagicEvent event, 
                final Object data[], 
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent perm) {
                        final MagicPermanent source = (MagicPermanent)event.getSource();
                        final MagicTargetFilter filter = new MagicTargetFilter.MagicPermanentTargetFilter(perm);
                        final MagicStatic S = new MagicStatic(MagicLayer.Control,filter) {
                                final int you = source.getController().getIndex();
                                @Override
                                public MagicPlayer getController(
                                    final MagicPermanent source, 
                                    final MagicPermanent permanent, 
                                    final MagicPlayer player) {
                                    return source.getController();
                                }
                                @Override
                                public boolean condition(
                                    final MagicGame game,
                                    final MagicPermanent source,
                                    final MagicPermanent target) {
                                    if (you != source.getController().getIndex()) {
                                        //remove this static after the update
                                        game.addDelayedAction(new MagicRemoveStaticAction(source, this));
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }
                            };
                        game.doAction(new MagicAddStaticAction(source, S));
                    };
                });
            }
        }
    };
}
