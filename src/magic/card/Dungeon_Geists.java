package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveStaticAction;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicAddTriggerAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenLoseControlTrigger;

public class Dungeon_Geists {
    private static final MagicTargetChoice victimChoice = new MagicTargetChoice(
	    MagicTargetFilter.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS, 
        true, 
        MagicTargetHint.Negative, 
        "target creature your opponent controls"
    );
    
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
            permanent,
            player,
            victimChoice,
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
                            @Override
                            public long getAbilityFlags(
                                final MagicGame game, 
                                final MagicPermanent permanent, 
                                final long flags) {
                                return flags | MagicAbility.DoesNotUntap.getMask();
                            }
                        };
                    game.doAction(new MagicAddStaticAction(source, S));
                    final MagicWhenLoseControlTrigger T = new MagicWhenLoseControlTrigger() {
                            @Override
                            public MagicEvent executeTrigger(
                                MagicGame game, 
                                MagicPermanent permanent, 
                                MagicPermanent data) {
                            	if (permanent.getId() == data.getId()) {
                            		game.doAction(new MagicRemoveStaticAction(permanent, S));
                            	}
                                return MagicEvent.NONE;
                            }
                        };
                    game.doAction(new MagicAddTriggerAction(source, T));
                }
            });
        }
    };
}
