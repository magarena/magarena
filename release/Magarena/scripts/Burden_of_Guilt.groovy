[
    new MagicPermanentActivation(
        [
            MagicCondition.ENCHANTED_IS_UNTAPPED_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getEnchantedPermanent(),
                this,
                "Tap RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new TapAction(event.getRefPermanent()));
        }
    }
]
