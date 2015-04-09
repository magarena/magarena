[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}"),
                new MagicTapEvent(source)
           ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                Other("target creature", source),
                MagicExileTargetPicker.create(),
                this,
                "Exile another target creature\$. Return that card to the battlefield under its owner's control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicExileUntilEndOfTurnAction(it));
            });
        }
    }
]
