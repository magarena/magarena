[
    new MagicCardActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setCardAct(this);
        }

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final int amount =  CREATURE_CARD_FROM_GRAVEYARD.filter(source.getController()).size();
            final int cost = Math.max(0,(4-amount));
            return [new MagicPayManaCostEvent(source,"{"+cost+"}{G}{G}")];
        }
    },
    
    new MagicPermanentActivation(
        [MagicCondition.NOT_MONSTROUS_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Monstrosity"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            final int amount =  CREATURE_CARD_FROM_GRAVEYARD.filter(source.getController()).size();
            final int cost = Math.max(0,(7-amount));
            return [
                new MagicPayManaCostEvent(source,"{"+cost+"}{G}{G}")
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
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,5));
            game.doAction(ChangeStateAction.Set(
                event.getPermanent(),
                MagicPermanentState.Monstrous
            ));
        }
    }
    
]
