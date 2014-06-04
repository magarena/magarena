def type = new MagicStatic(MagicLayer.Type) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags | MagicType.Artifact.getMask();
    }
};
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.Positive("target nonartifact creature"),
                MagicPumpTargetPicker.create(),
                this,
                "PN puts a +1/+1 counter on target nonartifact creature\$. That creature becomes an artifact in addition to its other types."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicAddStaticAction(creature,type));
                game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1,true));
            });
        }
    }
]
