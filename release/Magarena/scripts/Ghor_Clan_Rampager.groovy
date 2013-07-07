[
    new MagicBloodrushActivation(
        MagicManaCost.create("{R}{G}"),
        "Target attacking creature\$ gets +4/+4 and gains trample until end of turn."
    ) {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,4,4));
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Trample));
                }
            });
        }
    }
]
