[
    new MagicPermanentActivation(
        [MagicCondition.ANY_UPKEEP_CONDITION],
        new MagicActivationHints(MagicTiming.None),
        "Pump"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}"),
                new MagicSacrificePermanentEvent(source,SACRIFICE_LAND)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_CREATURE,
                this,
                "PN puts a +2/+2 counter on target creature.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeCountersAction(it, MagicCounterType.PlusTwo, 1));
            });
        }
    }
]
