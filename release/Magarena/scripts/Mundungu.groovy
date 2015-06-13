def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicEvent lifeEvent = new MagicPayLifeEvent(event.getSource(), event.getPlayer(), 1)
    if (event.isYes() && lifeEvent.isSatisfied()) {
        game.addEvent(lifeEvent);
    } else {
        game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Counter),
        "Counter"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
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
                    new MagicMayChoice(
                        "Pay {1} and 1 life?",
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
                    ),
                    card,
                    action,
                    "PN may\$ pay {1} and 1 life."
                ));
            });
        }
    }
]
