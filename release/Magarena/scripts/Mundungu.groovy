def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicPlayer player = event.getPlayer();
    final MagicEvent payLife = new MagicPayLifeEvent(event.getSource(), player, 1);
    final MagicEvent payMana = new MagicPayManaCostEvent(event.getSource(), player, "{1}");
    if (event.isYes() && payMana.isSatisfied() && payLife.isSatisfied()) {
        game.logAppendMessage(player,"(Yes)")
        game.addEvent(payMana);
        game.addEvent(payLife);
    } else {
        game.logAppendMessage(player,"(No)");
        game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {1} and 1 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetItemOnStack(game, {
                final MagicCardOnStack card ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    card.getController(),
                    new MagicMayChoice("Pay {1} and 1 life?"),
                    card,
                    action,
                    "PN may pay {1} and 1 life."
                ));
            });
        }
    }
]
