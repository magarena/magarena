package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicPayManaCostChoice;

public class MagicPayManaCostSacrificeEvent extends MagicEvent {
    
    public MagicPayManaCostSacrificeEvent(final MagicSource source,final MagicPlayer player,final MagicManaCost cost) {
        super(
            source,
            player,
            new MagicPayManaCostChoice(cost),
            EVENT_ACTION,
            "Pay "+cost.getText()+"$. Sacrifice SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            MagicEvent.payManaCost(game,event.getPlayer(),choiceResults,0);
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    };
}
