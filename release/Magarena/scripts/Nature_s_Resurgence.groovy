[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player draws a card for each creature card in his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                final int amount = CREATURE_CARD_FROM_GRAVEYARD.filter(player).size();
                game.logAppendMessage(event.getPlayer(), player.getName()+" draws "+amount+" cards.");
                game.doAction(new DrawAction(player, amount));
            }
        }
    }
]
