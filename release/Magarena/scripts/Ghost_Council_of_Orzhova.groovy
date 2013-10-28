[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal,1),
        "Exile"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicPlayAbilityEvent(source),
                new MagicSacrificePermanentEvent(source,MagicTargetChoice.SACRIFICE_CREATURE)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Exile SN. Return it to the battlefield under its owner's control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicExileUntilEndOfTurnAction(event.getPermanent()));
        }
    }
]
