[
    new MagicPermanentActivation(
        [
            MagicCondition.METALCRAFT_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
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
                "under PN's control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ExileUntilEndOfTurnAction(event.getPermanent()));
        }
    }
]
