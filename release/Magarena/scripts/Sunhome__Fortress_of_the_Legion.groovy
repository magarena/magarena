[
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{3}{R}{W}"), //add ONE for the card itself
            MagicCondition.CAN_TAP_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{2}{R}{W}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicFirstStrikeTargetPicker.create(),
                this,
                "Target creature\$ gains double strike until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.DoubleStrike));
                }
            });
        }
    }
]
