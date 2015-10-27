package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.RemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;

import java.util.Collection;
import magic.model.MagicMessage;

public class MagicUniquenessEvent extends MagicEvent {

    public MagicUniquenessEvent(final MagicSource source, final MagicTargetFilter<MagicPermanent> filter) {
        super(
            source,
            new MagicTargetChoice(
                filter,
                MagicTargetHint.None,
                "one. Put the rest into their owner's graveyard"
            ),
            EVENT_ACTION,
            "Choose one$. Put the rest into their owner's graveyard."
        );
    }

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    final Collection<MagicPermanent> targets = event.getTargetChoice().getPermanentFilter().filter(event);
                    for (final MagicPermanent target : targets) {
                        if (target != permanent) {
                            game.logAppendMessage(
                                event.getPlayer(),
                                String.format("Put %s into its owner's graveyard (Uniqueness rule).",
                                    MagicMessage.getCardToken(target)
                                )
                            );
                            game.doAction(new RemoveFromPlayAction(target,MagicLocationType.Graveyard));
                        }
                    }
                }
            });
        }
    };
}
