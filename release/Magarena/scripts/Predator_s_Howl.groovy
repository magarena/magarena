[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN creates a 2/2 green Wolf creature token. "+
                "PN creates three of those tokens instead if a creature died this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = game.getCreatureDiedThisTurn() ? 3 : 1;
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("2/2 green Wolf creature token"),
                amount
            ));
        }
    }
]
