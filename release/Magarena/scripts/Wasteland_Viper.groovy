[
    new MagicBloodrushActivation(
        MagicManaCost.create("{G}"),
        "Target attacking creature\$ gets +1/+2 and gains deathtouch until end of turn."
    ) {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,2));
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Deathtouch));
                }
            });
        }
    }
]
