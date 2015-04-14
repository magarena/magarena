package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.PlayCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicChoice;
import magic.model.target.MagicGraveyardTargetPicker;

import java.util.Arrays;
import java.util.List;

public class MagicPutOntoBattlefieldEvent extends MagicEvent {
    
    public MagicPutOntoBattlefieldEvent(final MagicEvent event, final MagicChoice choice, final List<? extends MagicPermanentAction> mods) {
        this(event.getSource(), event.getPlayer(), choice, mods);
    }
    
    public MagicPutOntoBattlefieldEvent(final MagicEvent event, final MagicChoice choice, final MagicPermanentAction... mods) {
        this(event.getSource(), event.getPlayer(), choice, Arrays.asList(mods));
    }
    
    public MagicPutOntoBattlefieldEvent(final MagicSource source, final MagicPlayer player, final MagicChoice choice, final List<? extends MagicPermanentAction> mods) {
        super(
            source,
            player,
            choice,
            MagicGraveyardTargetPicker.PutOntoBattlefield,
            EventAction(mods),
            ""
        );
    }

    private static final MagicEventAction EventAction(final List<? extends MagicPermanentAction> mods) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                // choice could be MagicMayChoice or MagicTargetChoice, the condition below takes care of both cases
                if (event.isNo() == false) {
                    event.processTargetCard(game, new MagicCardAction() {
                        public void doAction(final MagicCard card) {
                            game.logAppendMessage(event.getPlayer(), "Chosen (" + card + ").");
                            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
                            game.doAction(new PlayCardAction(card,event.getPlayer(),mods));
                        }
                    });
                }
            }
        };
    }
}
