package magic.model.event;

import java.util.Arrays;
import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicMessage;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicTuple;
import magic.model.action.MagicPermanentAction;
import magic.model.action.ReturnCardAction;
import magic.model.choice.MagicChoice;
import magic.model.target.MagicGraveyardTargetPicker;

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
            new MagicTuple(mods),
            EventAction,
            ""
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicTuple tup = event.getRefTuple();
        // choice could be MagicMayChoice or MagicTargetChoice, the condition below takes care of both cases
        if (!event.isNo()) {
            event.processTargetCard(game, (final MagicCard card) -> {
                game.logAppendMessage(
                    event.getPlayer(),
                    MagicMessage.format("Chosen (%s).", card)
                );
                game.doAction(new ReturnCardAction(MagicLocationType.OwnersHand,card,event.getPlayer(),tup.getMods()));
            });
        }
    };
}
