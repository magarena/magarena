def action = {
    final MagicGame game, final MagicEvent event ->
    final int hand = event.getPlayer().getHandSize();
    if (hand == 0) {
        game.doAction(new FlipAction(event.getPermanent()));
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Discard"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{1}{B}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ discards a card. Then if that player has no cards in hand, flip SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicDiscardEvent(event.getSource(), it));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    action,
                    ""
                ));
            });
        }
    }
]
