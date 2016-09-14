[
    new MagicPermanentActivation(
        [MagicCondition.FORMIDABLE],
        new MagicActivationHints(MagicTiming.Pump),
        "Attack"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{G}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN can attack this turn as though it didn't have defender."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainAbilityAction(event.getPermanent(), MagicAbility.CanAttackWithDefender));
        }
    }
]
