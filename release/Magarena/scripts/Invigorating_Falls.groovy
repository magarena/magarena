[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains life equal to the number of creature cards in all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = CREATURE_CARD_FROM_ALL_GRAVEYARDS.filter(event).size();
            game.doAction(new ChangeLifeAction(player,amount));
            game.logAppendValue(player,amount);
        }
    }
]
