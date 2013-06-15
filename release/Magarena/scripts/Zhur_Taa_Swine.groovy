[
    new MagicBloodrushActivation(
        MagicManaCost.create("{1}{R}{G}"),
        "Target attacking creature\$ gets +5/+4 until end of turn."
    ) {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,5,4));
                }
            });
        }
    }
]
