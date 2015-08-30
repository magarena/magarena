[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains " + amount + " life and draw " + amount + " cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            game.doAction(new ChangeLifeAction(event.getPlayer(), amount));
            game.doAction(new DrawAction(event.getPlayer(), amount));
        }
    }
]
