[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
    
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicTapEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy all artifacts, creatures and enchantments."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_ARTIFACT);
            targets.addAll(game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_CREATURE));
            targets.addAll(game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.TARGET_ENCHANTMENT));
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
