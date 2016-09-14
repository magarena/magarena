[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicDiscardEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "SN gets +1/+1 and becomes the color of PN's choice\$ until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent()
            final MagicColor color = event.getChosenColor();
            game.doAction(new ChangeTurnPTAction(permanent,1,1));
            game.doAction(new AddStaticAction(permanent,
                new MagicStatic(MagicLayer.Color,MagicStatic.UntilEOT) {
                @Override
                public int getColorFlags(final MagicPermanent perm, final int flags) {
                    return color.getMask();
                }
            }));
        }
    }
]
