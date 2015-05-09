[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 1/2 green Spider creature token with reach onto the battlefield "+
                "for each creature card in PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = 2 * game.filterCards(player, CREATURE_CARD_FROM_GRAVEYARD).size();
            game.doAction(new PlayTokensAction(player,CardDefinitions.getToken("1/2 green Spider creature token with reach"),amount));
        }
    }
]
