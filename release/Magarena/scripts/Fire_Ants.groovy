[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }
        
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN deals 1 damage to each other creature without flying."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource permanent = event.getPermanent();
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                    MagicTargetFilterFactory.CREATURE_WITHOUT_FLYING,
                    permanent
                );
            final Collection<MagicPermanent> creatures = game.filterPermanents(filter);
            for (final MagicPermanent creature : creatures) {
                    game.doAction(new MagicDealDamageAction(permanent, creature, 1));
            }
        }
    }
]
