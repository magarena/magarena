def choice = Negative("target creature");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Tapping),
        "Tap"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                choice,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target creature\$. It doesn't untap during its controller's untap step for as long as SN remains tapped."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent source = event.getPermanent();
                game.doAction(new TapAction(it));
                game.doAction(new AddStaticAction(
                    source,
                    MagicStatic.AsLongAsCond(it, MagicAbility.DoesNotUntap, MagicCondition.TAPPED_CONDITION)
                ));
            });
        }
    }
]
