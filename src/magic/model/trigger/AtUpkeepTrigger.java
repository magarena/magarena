package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicLocationType;
import magic.model.MagicAbility;
import magic.model.action.MagicPlayMod;
import magic.model.action.DrawAction;
import magic.model.action.RemoveTriggerAction;
import magic.model.action.ChangeCountersAction;
import magic.model.action.CastCardAction;
import magic.model.action.EnqueueTriggerAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class AtUpkeepTrigger extends MagicTrigger<MagicPlayer> {
    public AtUpkeepTrigger(final int priority) {
        super(priority);
    }

    public AtUpkeepTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.AtUpkeep;
    }

    public static AtUpkeepTrigger create(final MagicSourceEvent sourceEvent) {
        return new AtUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getTriggerEvent(permanent, upkeepPlayer);
            }
        };
    }

    public static AtUpkeepTrigger createOpp(final MagicSourceEvent sourceEvent) {
        return new AtUpkeepTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return permanent.isOpponent(upkeepPlayer);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getTriggerEvent(permanent, upkeepPlayer);
            }
        };
    }

    public static final AtUpkeepTrigger YouDraw(final MagicSource staleSource, final MagicPlayer stalePlayer) {
        return new AtUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                game.addDelayedAction(new RemoveTriggerAction(this));
                return new MagicEvent(
                    game.createDelayedSource(staleSource, stalePlayer),
                    this,
                    "PN draws a card."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new DrawAction(event.getPlayer()));
            }
        };
    }

    public static final AtUpkeepTrigger Suspend = new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            for (final MagicCard card : upkeepPlayer.getExile()) {
                if (card.hasAbility(MagicAbility.Suspend) && card.hasCounters(MagicCounterType.Time)) {
                    game.doAction(new EnqueueTriggerAction(new MagicEvent(
                        card,
                        this,
                        "If SN is suspended, remove a time counter from it."
                    )));
                }
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            if (card.isInExile() && card.hasAbility(MagicAbility.Suspend) && card.hasCounters(MagicCounterType.Time)) {
                game.doAction(new ChangeCountersAction(event.getPlayer(), card, MagicCounterType.Time, -1));
                game.logAppendMessage(event.getPlayer(), "Remove a time counter, there are " + card.getCounters(MagicCounterType.Time) + " time counters left.");
                if (!card.hasCounters(MagicCounterType.Time)) {
                    game.doAction(new EnqueueTriggerAction(new MagicEvent(
                        card,
                        event.getPlayer(),
                        this::playCard,
                        "If SN is exiled, PN plays it without paying its mana cost if able, otherwise it remains exiled."
                    )));
                }
            }
        }
        private void playCard(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            if (card.isInExile()) {
                game.doAction(CastCardAction.WithoutManaCost(
                    event.getPlayer(),
                    card,
                    MagicLocationType.Exile,
                    MagicLocationType.Graveyard,
                    MagicPlayMod.HASTE_SUSPEND
                ));
            }
        }
    };
}
