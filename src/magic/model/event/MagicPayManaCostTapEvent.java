package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicPayManaCostChoice;

public class MagicPayManaCostTapEvent extends MagicEvent {
    
    public MagicPayManaCostTapEvent(final MagicSource source,final MagicPlayer player,final MagicManaCost cost) {
        super(
            source,
            player,
            new MagicPayManaCostChoice(cost),
            EVENT_ACTION,
            "Pay "+cost.getText()+"$. Tap SN."
        );
    }            
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.payManaCost(game,event.getPlayer());
            game.doAction(new MagicTapAction(event.getPermanent(),true));
        }
    };
}
