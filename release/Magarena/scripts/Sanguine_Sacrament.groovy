[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = 2 * payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                amount,
                this,
                "PN gains ${amount} life. Put SN on the bottom of its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), event.getRefInt()));
            game.doAction(new ShiftCardAction(event.getCardOnStack().getCard(), MagicLocationType.Stack, MagicLocationType.BottomOfOwnersLibrary));
        }
    }
]

