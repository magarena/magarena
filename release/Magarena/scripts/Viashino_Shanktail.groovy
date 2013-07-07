[
    new MagicBloodrushActivation(
        MagicManaCost.create("{2}{R}"),
        "Target attacking creature\$ gets +3/+1 and gains first strike until end of turn."
    ) {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,3,1));
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.FirstStrike));
                }
            });
        }
    }
]
