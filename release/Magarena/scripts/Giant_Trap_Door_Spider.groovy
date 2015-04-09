def choice = Negative("target attacking creature without flying your opponent controls");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{1}{R}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                this,
                "Exile SN and target creature without flying that's attacking PN\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.Exile));
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.Exile));
            });
        }
    }
]
