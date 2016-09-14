[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Pump"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{3}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE,
                MagicDefaultTargetPicker.create(),
                this,
                "Target creature\$ gets +1/-1 for as long as SN remains tapped."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent source = event.getPermanent();
                game.doAction(new AddStaticAction(
                    source,
                    MagicStatic.AsLongAsCond(it, 1, -1, MagicCondition.TAPPED_CONDITION)
                ));
            });
        }
    }
]
