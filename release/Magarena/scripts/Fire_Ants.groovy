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
            final Collection<MagicPermanent> creatures = 
                game.filterPermanents(MagicTargetFilterFactory.CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent creature : creatures) {
                if (permanent != creature) {
                    final MagicDamage damage = new MagicDamage(permanent,creature,1);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            }
        }
    }
]
