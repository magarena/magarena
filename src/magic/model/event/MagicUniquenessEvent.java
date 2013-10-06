package magic.model.event;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.choice.MagicTargetChoice;
import magic.model.action.MagicPermanentAction;

public class MagicUniquenessEvent extends MagicEvent {

    public MagicUniquenessEvent(final MagicSource source, final MagicTargetFilter<MagicPermanent> filter) {
        super(
            source,
            new MagicTargetChoice(
                filter,
                MagicTargetHint.None,
                "one. Put the rest into their owner's graveyard"
            ),
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    event.processTargetPermanent(game,new MagicPermanentAction() {
                        public void doAction(final MagicPermanent permanent) {
                            final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(),filter);
                            for (final MagicPermanent target : targets) {
                                if (target != permanent) {
                                    game.logAppendMessage(
                                        event.getPlayer(),
                                        "Put " + target + " into its owner's graveyard (Uniqueness rule)."
                                    );
                                    game.doAction(new MagicRemoveFromPlayAction(target,MagicLocationType.Graveyard));
                                }
                            }
                        }
                    });
                }
            },
            "Choose one$. Put the rest into their owner's graveyard."
        );
    }
}
