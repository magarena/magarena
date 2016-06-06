[
    new MagicPlaneswalkerActivation(-5) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy all other permanents except for lands and tokens."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(
                NONLAND_NONTOKEN_PERMANENT
                .except(event.getPermanent())
                .filter(event)
            ));
        }
    }
]
