package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.MagicCard;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.action.RevealAction;
import magic.model.action.RemoveTriggerAction;
import magic.model.action.LookAction;
import magic.model.action.LoseGameAction;
import magic.model.choice.MagicMayChoice;

public abstract class AtYourUpkeepTrigger extends AtUpkeepTrigger {
    public AtYourUpkeepTrigger(final int priority) {
        super(priority);
    }

    public AtYourUpkeepTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        return permanent.isController(upkeepPlayer);
    }

    public static AtYourUpkeepTrigger create(final MagicSourceEvent sourceEvent) {
        return new AtYourUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static AtYourUpkeepTrigger kinship(final String effect, final MagicEventAction action) {
        return new AtYourUpkeepTrigger() {
            final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
                if (event.isYes()) {
                    final MagicCard card = event.getRefCard();
                    game.doAction(new RevealAction(event.getRefCard()));
                    action.executeEvent(game, event);
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
                                EVENT_ACTION,
                                ""
                            ));
                            break;
                        }
                    }
                }
            }
        };
    }

    public static final AtYourUpkeepTrigger PayOrLose(final MagicSource staleSource, final MagicPlayer stalePlayer, final String manaCost) {
        return new AtYourUpkeepTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return upkeepPlayer.getId() == stalePlayer.getId();
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                game.addDelayedAction(new RemoveTriggerAction(this));
                return new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    new MagicMayChoice("Pay " + manaCost + "?"),
                    this,
                    "PN pays " + manaCost + ". If PN doesn't he or she loses the game."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final MagicEvent cost = new MagicPayManaCostEvent(event.getSource(), manaCost);
                if (event.isYes() && cost.isSatisfied()) {
                    game.addEvent(cost);
                } else {
                    game.doAction(new LoseGameAction(event.getPlayer()));
                }
            }
        };
    }
}
