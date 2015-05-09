[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 2 life for each creature card in PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = CREATURE_CARD_FROM_GRAVEYARD.filter(event).size();
            game.doAction(new ChangeLifeAction(player,2 * amount));
        }
    }
]
