def choice = Positive("target elf creature");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                MagicPumpTargetPicker.create(),
                this,
                "Target Elf creature\$ gets +2/+2 and has trample for as long as SN remains tapped."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent source = event.getPermanent();
                game.doAction(new AddStaticAction(
                    source,
                    MagicStatic.AsLongAsCond(it, 2, 2, MagicCondition.TAPPED_CONDITION)
                ));
                game.doAction(new AddStaticAction(
                    source,
                    MagicStatic.AsLongAsCond(it, MagicAbility.Trample, MagicCondition.TAPPED_CONDITION)
                ));
            });
        }
    }
]
