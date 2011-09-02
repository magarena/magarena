package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Doomgape {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player=permanent.getController();
			return (player==data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player},
                        this,
                        "Sacrifice a creature. " + player + 
                        		" gains life equal to that creature's toughness."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[1];
			if (player.controlsPermanentWithType(MagicType.Creature)) {
				game.addEvent(new MagicEvent(
                    (MagicPermanent)data[0],
                    player,
                    MagicTargetChoice.SACRIFICE_CREATURE,
                    MagicSacrificeTargetPicker.getInstance(),
                    new Object[]{player},
                    new MagicEventAction() {
                        @Override
                        public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object[] data,
                            final Object[] choiceResults) {
                            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                                public void doAction(final MagicPermanent creature) {
                                    final int toughness=creature.getToughness(game);
                                    game.doAction(new MagicSacrificeAction(creature));
                                    game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],toughness));
                                }
                            });
		                }
	                },
                    "Choose a creature to sacrifice$."));
			}
		}
    };
}
