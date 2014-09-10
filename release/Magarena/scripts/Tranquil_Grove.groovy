[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{1}{G}{G}")];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            new MagicEvent(
                source,
                this,
                "Destroy all enchantments other than SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.ENCHANTMENT,
                event.getPermanent()
            );
            game.doAction(new MagicDestroyAction(
                game.filterPermanents(filter)
            ));
        }
    }
]
