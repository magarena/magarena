[
    new MagicPermanentActivation(
        [MagicCondition.NOT_MONSTROUS_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Monstrosity"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{X}{G}{G}{G}")
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
                game.doAction(new ChangeCountersAction(SN, MagicCounterType.PlusOne, event.getRefInt()));
                game.doAction(ChangeStateAction.Set(SN, MagicPermanentState.Monstrous));
            }
        }
    }
]
