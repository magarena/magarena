[
    new MagicBloodrushActivation(
        MagicManaCost.create("{3}{R}{R}"),
        "Target attacking creature\$ gets +3/+3 and gains double strike until end of turn."
    ) {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,3,3));
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.DoubleStrike));
                }
            });
        }
    }
]
