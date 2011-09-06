package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Armadillo_Cloak {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.getInstance());

    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
            final int amount=damage.getDealtAmount();
			return (permanent.getEnchantedCreature()==damage.getSource()) ?
				new MagicEvent(
                    permanent,
                    player,
                    MagicEvent.NO_DATA,
                    new MagicEventAction() {
                    @Override
                    public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object data[],
                            final Object[] choiceResults) {
                        game.doAction(new MagicChangeLifeAction(player.map(game),amount));
                    }},
                    player + "gains " + amount + " life."):
                MagicEvent.NONE;
		}
    };
}
