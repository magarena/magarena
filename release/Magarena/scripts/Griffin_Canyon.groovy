def choice = new MagicTargetChoice("target Griffin");
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Untap"
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
                MagicTapTargetPicker.Untap,
                this,
                "PN untaps target Griffin\$. If it's a creature it gets +1/+1 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new UntapAction(it));
                if (it.isCreature()) {
                    game.doAction(new ChangeTurnPTAction(it, 1, 1));
                }
            });
        }
    }
]
