package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.MagicCard;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEventAction;
import magic.model.action.RevealAction;
import magic.model.action.LookAction;
import magic.model.choice.MagicMayChoice;

public abstract class MagicAtYourUpkeepTrigger extends AtUpkeepTrigger {
    public MagicAtYourUpkeepTrigger(final int priority) {
        super(priority);
    }

    public MagicAtYourUpkeepTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        return permanent.isController(upkeepPlayer);
    }

    public static MagicAtYourUpkeepTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicAtYourUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    public static MagicAtYourUpkeepTrigger kinship(final String effect, final MagicEventAction action) {
        return new MagicAtYourUpkeepTrigger() {
            final MagicEventAction ACTION = new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    if (event.isYes()) {
                        final MagicCard card = event.getRefCard();
                        game.doAction(new RevealAction(event.getRefCard()));
                        action.executeEvent(game, event);
                    }
                }
            };

            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                return new MagicEvent(
                    permanent,
                    this,
                    "PN looks at the top card of his or her library. If it shares a creature type with SN, PN may reveal it. " +
                    "If PN does, " +  effect
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                    game.doAction(new LookAction(card, event.getPlayer(), "top card of your library"));
                    for (final MagicSubType st : MagicSubType.ALL_CREATURES) {
                        if (card.hasSubType(st) && event.getPermanent().hasSubType(st)) {
                            game.addEvent(new MagicEvent(
                                event.getSource(),
                                new MagicMayChoice("Reveal the top card of your library?"),
                                card,
                                ACTION,
                                ""
                            ));
                            break;
                        }
                    }
                }
            }
        };
    }
}
