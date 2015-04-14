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
            CREATURE_WITHOUT_FLYING
            .except(permanent)
            .filter(game) each {
                game.doAction(new DealDamageAction(permanent, it, 1));
            }
        }
    }
]
