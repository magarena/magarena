[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main),
        "+1") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                MagicPutCounterEvent.Self(
                    source,
                    MagicCounterType.Charge,
                    1
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicMayChoice(
                    MagicTargetChoice.POS_TARGET_CREATURE
                ),
                MagicPumpTargetPicker.create(),
                this,
                "PN may\$ put a +1/+1 counter on target creature\$."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1,true));
                    }
                });
            }
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION,
            MagicConditionFactory.ChargeCountersAtLeast(3)
        ],
        new MagicActivationHints(MagicTiming.Main),
        "-3") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(
                    source,
                    MagicCounterType.Charge,
                    3
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicFlyingTargetPicker.create(),
                this,
                "Target creature\$ gains flying and double strike until end of turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Flying,MagicAbility.DoubleStrike));
                }
            });
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION,
            MagicConditionFactory.ChargeCountersAtLeast(8)
        ],
        new MagicActivationHints(MagicTiming.Main),
        "-8") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(
                    source,
                    MagicCounterType.Charge,
                    8
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put X 2/2 white Cat creature tokens onto the battlefield, where X is your life total."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int amt = event.getPlayer().getLife();
            game.doAction(new MagicPlayTokensAction(event.getPlayer(), TokenCardDefinitions.get("Cat2"), amt));
        }
    }
]
