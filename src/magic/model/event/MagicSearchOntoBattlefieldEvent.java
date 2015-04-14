package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.PlayCardAction;
import magic.model.action.MagicPlayMod;
import magic.model.action.RemoveCardAction;
import magic.model.action.ShuffleLibraryAction;
import magic.model.action.AIRevealAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicCardChoiceResult;
import magic.model.target.MagicGraveyardTargetPicker;

import java.util.Arrays;
import java.util.List;

public class MagicSearchOntoBattlefieldEvent extends MagicEvent {
    public MagicSearchOntoBattlefieldEvent(final MagicEvent event, final MagicChoice choice, final MagicPermanentAction... mods) {
        this(event.getSource(), event.getPlayer(), choice, Arrays.asList(mods));
    }
    
    public MagicSearchOntoBattlefieldEvent(final MagicEvent event, final MagicChoice choice, final List<? extends MagicPermanentAction> mods) {
        this(event.getSource(), event.getPlayer(), choice, mods);
    }
    
    public MagicSearchOntoBattlefieldEvent(final MagicSource source, final MagicPlayer player, final MagicChoice choice, final MagicPermanentAction... mods) {
        this(source, player, choice, Arrays.asList(mods));
    }

    public MagicSearchOntoBattlefieldEvent(final MagicSource source, final MagicPlayer player, final MagicChoice choice, final List<? extends MagicPermanentAction> mods) {
        super(
            source,
            player,
            choice,
            MagicGraveyardTargetPicker.PutOntoBattlefield,
            EventAction(mods),
            ""
        );
    }
    
    @Override
    public void onAddEvent(final MagicGame game) {
        game.doAction(new AIRevealAction(getPlayer().getLibrary()));
    }

    private static final MagicEventAction EventAction(final List<? extends MagicPermanentAction> mods) {
        return new MagicEventAction() {
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                // choice could be MagicMayChoice or MagicTargetChoice or MagicFromCardListChoice
                if (event.isNo()) {
                    game.doAction(AIRevealAction.Hide(event.getPlayer().getLibrary()));
                } else if (event.getChosen()[0] instanceof MagicCardChoiceResult) {
                    event.processChosenCards(game, new MagicCardAction() {
                        public void doAction(final MagicCard card) {
                            game.logAppendMessage(event.getPlayer(), "Found (" + card + ").");
                            game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersLibrary));
                            game.doAction(new PlayCardAction(card,event.getPlayer(),mods));
                        }
                    });
                    game.doAction(new ShuffleLibraryAction(event.getPlayer()));
                } else {
                    event.processTargetCard(game, new MagicCardAction() {
                        public void doAction(final MagicCard card) {
                            game.logAppendMessage(event.getPlayer(), "Found (" + card + ").");
                            game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersLibrary));
                            game.doAction(new PlayCardAction(card,event.getPlayer(),mods));
                        }
                    });
                    game.doAction(new ShuffleLibraryAction(event.getPlayer()));
                }
            }
        };
    }
}
