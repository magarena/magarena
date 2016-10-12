[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player creates a green Elephant creature token. "+
                "Those creatures have \"This creature's power and toughness are each equal to "+
                "the number of creature cards in its controller's graveyard.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                game.doAction(new PlayTokenAction(
                    player,
                    CardDefinitions.getToken("green Elephant creature token")
                ))
            }
        }
    }
]
