[
    new MagicBloodrushActivation(
        MagicManaCost.create("{G}"),
        "Target attacking creature\$ gets +3/+2 until end of turn."
    ) {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,3,2));
                }
            });
        }
    }
]
