[
    new MagicBloodrushActivation(
        MagicConditionFactory.ManaCost("{R}"),
        "Target attacking creature\$ gets +2/+1 until end of turn.") {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,2,1));
                }
            });
        }
    }
]
