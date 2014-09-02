[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Draw"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws two cards, then puts a card from his hand on top of his library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),2));
	    game.addEvent(new MagicReturnCardEvent(event.getSource(), event.getPlayer()));
        }
    }
]
