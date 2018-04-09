[
    new MagicPermanentActivation(
        [MagicCondition.NOT_MONSTROUS_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Monstrosity"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            final int n =  CREATURE_CARD_FROM_GRAVEYARD.filter(source.getController()).size();
            return [
                new MagicPayManaCostEvent(
                    source,
                    MagicManaCost.create("{7}{G}{G}").reduce(n)
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "If SN isn't monstrous, put five +1/+1 counters on it and it becomes monstrous."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(),event.getPermanent(),MagicCounterType.PlusOne,5));
            game.doAction(ChangeStateAction.Set(
                event.getPermanent(),
                MagicPermanentState.Monstrous
            ));
        }
    }
]
