[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{U}"),
            MagicCondition.METALCRAFT_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Removal,false,1),
        "Exile"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{U}"),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Exile SN. Return it to the battlefield " +
                "under your control at the end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicExileUntilEndOfTurnAction(event.getPermanent()));
        }
    }
]
