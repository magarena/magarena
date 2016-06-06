[
    new MagicPlaneswalkerActivation(-8) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts X 2/2 white Cat creature tokens onto the battlefield, where X is his or her life total."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.getPlayer().getLife();
            game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("2/2 white Cat creature token"), amt));
        }
    }
]
