def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetPermanent(game, {
            game.doAction(new ShiftCardAction(it, MagicLocationType.Battlefield, MagicLocationType.OwnersHand));
        });
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "Draw"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent("{3}{U}");
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer()));
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice(A_LAND_YOU_CONTROL),
                action,
                "PN may\$ return a land PN controls\$ to its owner's hand."
            ));
        }
    }
]

