[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains 4 life. " +
                "PN gains 8 life instead if a creature died this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = game.getCreatureDiedThisTurn() ? 8 : 4;
            game.doAction(new ChangeLifeAction(event.getPlayer(), amount));
        }
    }
]
