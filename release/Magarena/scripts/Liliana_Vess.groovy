[
    new MagicPlaneswalkerActivation(-8) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put all creature cards from all graveyards onto the battlefield under PN's control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(event) each {
                game.doAction(new ReanimateAction(
                    it,
                    event.getPlayer()
                ));
            }
        }
    }
]
