[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{W}{W}")],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Indestr"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{W}{W}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicIndestructibleTargetPicker.create(),
                this,
                "Target creature\$ is indestructible this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Indestructible));
                }
            });
        }
    }
]
