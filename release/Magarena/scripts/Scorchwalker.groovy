[
    new MagicBloodrushActivation(
        MagicManaCost.create("{1}{R}{R}"),
        "Target attacking creature\$ gets +5/+1 until end of turn."
    ) {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,5,1));
                }
            });
        }
    }
]
