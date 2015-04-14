[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent the next "+amount+" damage that would be dealt to PN this turn. Draw a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            game.doAction(new MagicPreventDamageAction(event.getPlayer(), amount));
            game.doAction(new DrawAction(event.getPlayer()));
        }
    }
]
