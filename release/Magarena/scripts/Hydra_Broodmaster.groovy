def PutHydra = new MagicTrigger<Integer>() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent perm, final Integer X) {
        return new MagicEvent(
            perm,
            X,
            this,
            "Put RN RN/RN green Hydra creature tokens onto the battlefield."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final int X = event.getRefInt();
        final MagicStatic PT = new MagicStatic(MagicLayer.SetPT) {
            @Override
            public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                pt.set(X,X);
            }
        };
        for (int i = 0; i < X; i++) {
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("green Hydra creature token"),
                {
                    final MagicPermanent perm ->
                    game.doAction(new MagicAddStaticAction(perm,PT));
                }
            ));
        }
    }
    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesState;
    }
};

[
    new MagicPermanentActivation(
        [MagicCondition.NOT_MONSTROUS_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Monstrosity"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{X}{X}{G}")
            ];
        }
        
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "If SN isn't monstrous, put RN +1/+1 counter on it and it becomes monstrous."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                event.getRefInt(),
                true
            ));
            final MagicChangeStateAction act = MagicChangeStateAction.Set(
                event.getPermanent(),
                MagicPermanentState.Monstrous
            );
            game.doAction(act);
            game.executeTrigger(PutHydra, event.getPermanent(), event.getSource(), event.getRefInt());
        }
    }
]
