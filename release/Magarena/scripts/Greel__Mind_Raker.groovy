[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Discard"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{X}{B}"),
                new MagicTapEvent(source),
                new MagicDiscardEvent(source, 2),
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                payedCost.getX(),          
                this,
                "Target player\$ discards RN cards at random."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(MagicDiscardEvent.Random(event.getSource(), it, event.getRefInt()));
            });
        }
    }
]
