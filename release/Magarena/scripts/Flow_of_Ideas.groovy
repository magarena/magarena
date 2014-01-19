[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws a card for each Island PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = game.filterCards(player, MagicTargetFilter.TARGET_ISLAND_YOU_CONTROL).size();
            game.doAction(new MagicDrawAction(player,amount));
        }
    }
]
