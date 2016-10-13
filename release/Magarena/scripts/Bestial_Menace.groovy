[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN creates a 1/1 green Snake creature token, " +
                           "a 2/2 green Wolf creature token and " +
                           "a 3/3 green Elephant creature token."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            game.doAction(new PlayTokenAction(player,CardDefinitions.getToken("1/1 green Snake creature token")));
            game.doAction(new PlayTokenAction(player,CardDefinitions.getToken("2/2 green Wolf creature token")));
            game.doAction(new PlayTokenAction(player,CardDefinitions.getToken("3/3 green Elephant creature token")));
        }
    }
]
