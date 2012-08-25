package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveStaticAction;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Dungeon_Geists {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
            permanent,
            player,
            MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS, 
            MagicEvent.NO_DATA,
            this,
            "Tap target creature opponent controls$. " + 
            "It doesn't untap during its controller's untap step as long as " + player + " controls " + permanent + ".");
        }

        @Override
        public void executeEvent(
                final MagicGame game, 
                final MagicEvent event, 
                final Object data[], 
                final Object[] choiceResults) {
            event.processTargetPermanent(game, choiceResults, 0, new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    final MagicPermanent source = (MagicPermanent)event.getSource();
                    game.doAction(new MagicTapAction(perm, true));
                    final MagicTargetFilter filter = new MagicTargetFilter.MagicPermanentTargetFilter(perm);
                    final MagicStatic S = new MagicStatic(MagicLayer.Ability,filter) {
                            final int you = source.getController().getIndex();
                            @Override
                            public long getAbilityFlags(
                                final MagicPermanent source, 
                                final MagicPermanent permanent, 
                                final long flags) {
                                return flags | MagicAbility.DoesNotUntap.getMask();
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
                }
            });
        }
    };
}
