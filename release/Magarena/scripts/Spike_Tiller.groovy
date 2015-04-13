def PT = new MagicStatic(MagicLayer.SetPT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(2,2);
    }
};
def ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Animate),
        "Becomes"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}"),
                new MagicRemoveCounterEvent(source,MagicCounterType.PlusOne,1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_LAND,
                this,
                "Target land becomes a 2/2 creature that's still a land. Put a +1/+1 counter on it."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new BecomesCreatureAction(it,PT,ST));
                game.doAction(new MagicChangeCountersAction(it,MagicCounterType.PlusOne,1));
            });
        }
    }
]
