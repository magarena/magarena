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
            final MagicTargetFilter<MagicPermanent> filter = new MagicOtherPermanentTargetFilter(
                MagicTargetFilterFactory.CREATURE_WITH_FLYING,
                permanent
            );
            final Collection<MagicPermanent> targets = game.filterPermanents(filter);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicDealDamageAction(permanent,target,6));
            }
        }
    }
]
