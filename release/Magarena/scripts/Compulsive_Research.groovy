def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicEvent discardEvent = new MagicDiscardChosenEvent(event.getSource(), event.getPlayer(), LAND_CARD_FROM_HAND)
    if (event.isYes() && discardEvent.isSatisfied()) {
        game.addEvent(discardEvent);
    } else {
        game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), 2));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ draws three cards. Then that player discards two cards unless he or she discards a land card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DrawAction(it, 3));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    new MagicMayChoice("Discard a land card?"),
                    action,
                    ""
                ));
            })
        }
    }
]

