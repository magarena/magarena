def choice = Positive("target goblin creature");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                MagicPumpTargetPicker.create(),
                this,
                "Target Goblin creature\$ gets +2/+2 and has haste for as long as SN remains tapped."
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
                    MagicStatic.AsLongAsCond(it, MagicAbility.Haste, MagicCondition.TAPPED_CONDITION)
                ));
            });
        }
    }
]
