[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 2/2 green Wolf creature token onto the battlefield for each Forest PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("2/2 green Wolf creature token"),
                player.getNrOfPermanents(FOREST)
            ));
        }
    }
]
