[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Control"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{5}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_NONLAND_PERMANENT,
                MagicDefaultTargetPicker.create(),
                this,
                "PN exchanges control of SN and target nonland permanent\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicGainControlAction(event.getPlayer(),it));
                game.doAction(new MagicGainControlAction(it.getController(),event.getPermanent()));
            });
        }
    }
]
