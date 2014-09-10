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
            final MagicPermanent permanent=event.getPermanent();
            final MagicTargetFilter<MagicPermanent> targetFilter =
                    new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.ENCHANTMENT,permanent);
            final Collection<MagicPermanent> targets=
                game.filterPermanents(permanent.getController(),targetFilter);
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
