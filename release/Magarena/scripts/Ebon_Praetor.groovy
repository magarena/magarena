[
    new MagicPermanentActivation(
        [MagicCondition.YOUR_UPKEEP_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(source,SACRIFICE_CREATURE),
                new MagicPlayAbilityEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "Remove a -2/-2 counter from SN. " +
                "If the sacrificed creature was a Thrull, PN puts a +1/+0 counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPlayer(),
                event.getPermanent(),
                MagicCounterType.MinusTwo,
                -1
            ));
            if (event.getRefPermanent().hasSubType(MagicSubType.Thrull)) {
                game.doAction(new ChangeCountersAction(
                    event.getPlayer(),
                    event.getPermanent(),
                    MagicCounterType.PlusOnePlusZero,
                    1
                ));
            }
        }
    }
]
