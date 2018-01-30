def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetPermanent(game, {
            game.doAction(new RemoveFromPlayAction(it, MagicLocationType.OwnersHand));
        });
    } else {
        game.addEvent(new MagicDiscardEvent(event.getSource(), event.getPlayer(), 1));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN draws 2 cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), 2));
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice(A_LAND_YOU_CONTROL),
                action,
                "PN discards a card unless PN returns a land PN controls to its owner's hand\$\$."
            ));
        }
    }
]

