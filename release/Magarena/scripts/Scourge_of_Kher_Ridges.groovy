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
            CREATURE_WITH_FLYING
            .except(event.getPermanent())
            .filter(game) each {
                game.doAction(new DealDamageAction(event.getPermanent(),it,6));
            }
        }
    }
]
