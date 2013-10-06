[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                new MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,
                    source
                ),
                MagicTargetHint.None,
                "a creature other than " + source + " to sacrifice"
            );
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    targetChoice
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Untap SN and put a +1/+1 counter on it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicUntapAction(event.getPermanent()));
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                1,
                true
            ));
        }
    },
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target creature\$ your opponent controls, " +
                    "then put a +1/+1 counter on SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDestroyAction(creature));
                    game.doAction(new MagicChangeCountersAction(
                        event.getPermanent(),
                        MagicCounterType.PlusOne,
                        1,
                        true
                    ));
                }
            });
        }
    }
]
