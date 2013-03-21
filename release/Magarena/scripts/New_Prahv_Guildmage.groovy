[
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{W}{U}")],
        new MagicActivationHints(MagicTiming.Pump,true),
        "Flying"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{W}{U}"))];
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
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying));
                }
            });
        }
    },
    new MagicPermanentActivation(
        [MagicConditionFactory.ManaCost("{3}{W}{U}")],
        new MagicActivationHints(MagicTiming.FirstMain,true),
        "Detain"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{3}{W}{U}"))];
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
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicDetainAction(creature));
                }
            });
        }    
    }
]
