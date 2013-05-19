[
    new MagicPermanentActivation(
        [
            MagicCondition.ENCHANTED_IS_UNTAPPED_CONDITION,
            MagicConditionFactory.ManaCost("{1}")
        ],
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(
                source,
                source.getController(),
                MagicManaCost.create("{1}")
            )];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getEnchantedCreature(),
                this,
                "Tap RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicTapAction(event.getRefPermanent(),true));
        }
    }
]
