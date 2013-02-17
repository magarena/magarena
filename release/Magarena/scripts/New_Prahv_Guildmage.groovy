[
    new MagicPermanentActivation(
        [MagicManaCost.WHITE_BLUE.getCondition()],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Flying"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.WHITE_BLUE)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicFlyingTargetPicker.create(),
                this,
                "Target creature\$ gains flying until end of turn."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying));
                }
            });
        }
    },
    new MagicPermanentActivation(
        [MagicManaCost.THREE_WHITE_BLUE.getCondition()],
        new MagicActivationHints(MagicTiming.FirstMain,true),
        "Detain"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_WHITE_BLUE)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_NONLAND_PERMANENT_YOUR_OPPONENT_CONTROLS,
                new MagicNoCombatTargetPicker(true,true,false),
                this,
                "Detain target nonland permanent\$ an opponent controls."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDetainAction(creature));
                }
            });
        }    
    }
]
