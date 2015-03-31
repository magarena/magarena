[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "6 Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{5}{R}")];
        }
        
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN deals 6 damage to each other creature with flying."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource permanent = event.getPermanent();
            final Collection<MagicPermanent> creatures = 
                game.filterPermanents(MagicTargetFilterFactory.CREATURE_WITH_FLYING);
            for (final MagicPermanent creature : creatures) {
                if (permanent != creature) {
                    game.doAction(new MagicDealDamageAction(permanent,creature,6));
                }
            }
        }
    }
]
