package magic.model.event;

import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.choice.MagicCoinFlipChoice;

public class MagicCoinFlipEvent extends MagicEvent {

    public MagicCoinFlipEvent(final MagicEvent event, final MagicEventAction winAction, final MagicEventAction loseAction) {
        this(event, event.getRef(), winAction, loseAction);
    }

    public MagicCoinFlipEvent(final MagicEvent event, final int ref, final MagicEventAction winAction, final MagicEventAction loseAction) {
        super(
            event.getSource(),
            event.getPlayer(),
            MagicCoinFlipChoice.create(),
            ref,
            EventAction(winAction, loseAction),
            "Choose one$ — (1) Heads; (2) Tails. PN flips a coin."
        );
    }

    public MagicCoinFlipEvent(final MagicEvent event, final MagicCopyable ref, final MagicEventAction winAction, final MagicEventAction loseAction) {
        super(
            event.getSource(),
            event.getPlayer(),
            MagicCoinFlipChoice.create(),
            ref,
            EventAction(winAction, loseAction),
            "Choose one$ — (1) Heads; (2) Tails. PN flips a coin$."
        );
    }

    public static final MagicEventAction EventAction(final MagicEventAction winAction, final MagicEventAction loseAction) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                //True = Heads
                final MagicPlayer player = event.getPlayer();
                boolean coinFace = (player.getStateId() & 1) == 0;
                boolean playerFace = event.isMode(1);
                if (coinFace) {
                    game.logAppendMessage(player, "Coin landed heads.");
                    //Heads trigger
                } else {
                    game.logAppendMessage(player, "Coin landed tails.");
                    //Tails trigger
                }
                if (playerFace == coinFace) {
                    game.logAppendMessage(player, player.getName() + " wins.");
                    winAction.executeEvent(game, event);
                    //Win trigger
                } else {
                    game.logAppendMessage(player, player.getName() + " loses.");
                    loseAction.executeEvent(game, event);
                    //Lose trigger
                }
            }
        };
    }
}

