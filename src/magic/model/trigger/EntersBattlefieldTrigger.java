package magic.model.trigger;

import magic.data.CardDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.ChangeCountersAction;
import magic.model.action.ChangeStateAction;
import magic.model.action.PlayTokensAction;
import magic.model.action.SacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSourceEvent;

public abstract class EntersBattlefieldTrigger extends MagicTrigger<MagicPayedCost> {

    public static EntersBattlefieldTrigger create(final MagicSourceEvent sourceEvent) {
        return new EntersBattlefieldTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
                return sourceEvent.getTriggerEvent(permanent, payedCost);
            }
        };
    }

    public static final EntersBattlefieldTrigger createKicked(final MagicSourceEvent sourceEvent) {
        return new EntersBattlefieldTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
                return sourceEvent.getTriggerEvent(permanent, payedCost);
            }
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPayedCost payedCost) {
                return payedCost.isKicked();
            }
        };
    }

    public EntersBattlefieldTrigger(final int priority) {
        super(priority);
    }

    public EntersBattlefieldTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenComesIntoPlay;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }

    public static final EntersBattlefieldTrigger ChooseOpponent = new EntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            permanent.setChosenPlayer(permanent.getOpponent());
            return MagicEvent.NONE;
        }
    };

    public static final EntersBattlefieldTrigger ChoosePlayer = new EntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(MagicGame game, MagicPermanent permanent, MagicPayedCost data) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.Negative("a Player"),
                this,
                "PN chooses a player$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.getPermanent().setChosenPlayer(event.getChosenPlayer());
        }
    };

    public static final EntersBattlefieldTrigger Evoke = new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return payedCost.isKicked() ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getPermanent()));
        }
    };

    public static final EntersBattlefieldTrigger Exploit = new EntersBattlefieldTrigger() {
        private final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
            event.processTargetPermanent(game, (final MagicPermanent permanent) -> {
                game.doAction(ChangeStateAction.Set(event.getPermanent(), MagicPermanentState.Exploit));
                game.doAction(new SacrificeAction(permanent));
            });

        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice a creature?"),
                this,
                "PN may$ sacrifice a creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                MagicTargetChoice.A_CREATURE_YOU_CONTROL,
                EVENT_ACTION
            );
            if (event.isYes() && sac.isSatisfied()) {
                game.addEvent(sac);
            }
        }
    };

    public static final EntersBattlefieldTrigger Fabricate(final int n) {
        return new EntersBattlefieldTrigger() {
            private final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
                if (event.isYes()) {
                    game.doAction(new ChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        event.getRefInt()
                    ));
                    game.logAppendMessage(
                        event.getPlayer(),
                        event.getPlayer() + " puts " + event.getRefInt() + " +1/+1 counters on " + event.getPermanent() + "."
                    );
                } else {
                    game.doAction(new PlayTokensAction(
                        event.getPlayer(),
                        CardDefinitions.getToken("1/1 colorless Servo artifact creature token"),
                        event.getRefInt()
                    ));
                }
            };

            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    permanent,
                    n,
                    this,
                    "Fabriate RN."
                );
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (event.getPermanent().isValid()) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        event.getPlayer(),
                        new MagicMayChoice(
                            MagicMessage.replaceName(
                                "Put RN +1/+1 counters on SN? If you don’t, create RN Servo tokens.",
                                event.getSource(),
                                event.getPlayer(),
                                event.getRefInt()
                            )
                        ),
                        event.getRefInt(),
                        EVENT_ACTION,
                        "You may$ put RN +1/+1 counters on SN. If you don’t, create RN 1/1 colorless Servo artifact creature tokens."
                    ));
                } else {
                    game.doAction(new PlayTokensAction(
                        event.getPlayer(),
                        CardDefinitions.getToken("1/1 colorless Servo artifact creature token"),
                        event.getRefInt()
                    ));
                }
            }
        };
    }
}
