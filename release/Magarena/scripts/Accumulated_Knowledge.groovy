[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws a card, then draw cards equal to the number of cards named "+
                "Accumulated Knowledge in all graveyards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = cardName("Accumulated Knowledge")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.OpponentsGraveyard)
                .filter(event)
                .size();
            game.doAction(new DrawAction(event.getPlayer()));
            game.logAppendX(event.getPlayer(),amount);
            game.doAction(new DrawAction(event.getPlayer(),amount));
        }
    }
]
