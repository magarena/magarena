def choice = Negative("target Sliver");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                MagicExileTargetPicker.create(),
                this,
                "PN gains control of target Sliver\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainControlAction(
                    event.getPlayer(),
                    it
                ));
            });
        }
    }
]
