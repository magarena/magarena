package magic.model.event;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;

public class MagicPlayOgreUnlessEvent extends MagicEvent {

    public MagicPlayOgreUnlessEvent(final MagicSource source,final MagicPlayer player,final MagicPlayer controller,final MagicManaCost cost) {
        super(
            source,
            player,
            new MagicMayChoice(
                new MagicPayManaCostChoice(cost)
            ),
            controller,
            EVENT_ACTION,
            "You may$ pay "+cost.getText()+"$."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.payManaCost(game);
            } else {
                game.doAction(new MagicPlayTokenAction(event.getRefPlayer(),TokenCardDefinitions.get("Ogre")));
            }
        }
    };
}
