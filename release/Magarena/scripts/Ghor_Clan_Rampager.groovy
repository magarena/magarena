[
    new MagicBloodrushActivation(MagicManaCost.RED_GREEN) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_ATTACKING_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target attacking creature\$ gets +4/+4 and gains trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,4,4));
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
                }
            });
        }
    }
]
