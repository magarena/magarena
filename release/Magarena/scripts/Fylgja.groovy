[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(source, MagicCounterType.Healing, 1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getEnchantedPermanent(),
                this,
                "Prevent the next 1 damage that would be dealt to RN this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
           game.doAction(new PreventDamageAction(event.getRefPermanent(), 1));
        }
    }
]
