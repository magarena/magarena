package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Suture_Priest {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player=permanent.getController();
            final MagicPlayer controller=otherPermanent.getController();
            final boolean same=controller==player;
			return (otherPermanent!=permanent&&otherPermanent.isCreature()) ?
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
                        game.doAction(new MagicChangeLifeAction(controller.map(game),same?1:-1));
                    }},
                    controller + (same ? " gains 1 life." : " loses 1 life.")):
                MagicEvent.NONE;
		}
    };
}
