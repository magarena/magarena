package magic.model.event;

import java.util.Arrays;
import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicTuple;
import magic.model.action.AIRevealAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.ReturnCardAction;
import magic.model.action.ShuffleLibraryAction;
import magic.model.choice.MagicCardChoiceResult;
import magic.model.choice.MagicChoice;
import magic.model.target.MagicGraveyardTargetPicker;

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
            new MagicTuple(mods),
            EventAction,
            ""
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicTuple tup = event.getRefTuple();
        // choice could be MagicMayChoice or MagicTargetChoice or MagicFromCardListChoice
        if (event.isNo()) {
            // do nothing
        } else if (event.getChosen()[0] instanceof MagicCardChoiceResult) {
            event.processChosenCards(game, (final MagicCard card) -> {
                game.logAppendMessage(
                    event.getPlayer(),
                    MagicMessage.format("Found (%s).", card)
                );
                game.doAction(new AIRevealAction(card));
                game.doAction(new ReturnCardAction(card.getLocation(),card,event.getPlayer(),tup.getMods()));
            });
            game.doAction(new ShuffleLibraryAction(event.getPlayer()));
        } else {
            event.processTargetCard(game, (final MagicCard card) -> {
                game.logAppendMessage(
                    event.getPlayer(),
                    MagicMessage.format("Found (%s).", card)
                );
                game.doAction(new AIRevealAction(card));
                game.doAction(new ReturnCardAction(card.getLocation(),card,event.getPlayer(),tup.getMods()));
            });
            game.doAction(new ShuffleLibraryAction(event.getPlayer()));
        }
    };
}
