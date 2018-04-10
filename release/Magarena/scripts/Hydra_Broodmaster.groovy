def PutHydra = new MagicTrigger<Integer>() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent perm, final Integer X) {
        return new MagicEvent(
            perm,
            X,
            this,
            "PN creates RN RN/RN green Hydra creature tokens."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final int x = event.getRefInt();
        game.doAction(new PlayTokensAction(
            event.getPlayer(),
            CardDefinitions.getToken(x, x, "green Hydra creature token"),
            x
        ));
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
            final MagicPermanent SN = event.getPermanent();
            if (MagicCondition.NOT_MONSTROUS_CONDITION.accept(SN)) {
                game.doAction(new ChangeCountersAction(event.getPlayer(), SN, MagicCounterType.PlusOne, event.getRefInt()));
                game.doAction(ChangeStateAction.Set(SN, MagicPermanentState.Monstrous));
                game.executeTrigger(PutHydra, SN, SN, event.getRefInt());
            }
        }
    }
]
