[
    new MagicPlaneswalkerActivation(-6) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a white Avatar creature token onto the battlefield. " +
                "It has \"This creature's power and toughness are each equal to your life total.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(event.getPlayer(), CardDefinitions.getToken("white Avatar creature token")));
        }
    }
]
