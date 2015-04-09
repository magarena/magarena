def color = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return MagicColor.White.getMask();
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.None),
        "Color"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{W}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PERMANENT,
                this,
                "Target permanent\$ becomes white until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicAddStaticAction(it, color));
            });
        }
    }
]
