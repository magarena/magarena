[
    new MagicPermanentActivation(
        [MagicCondition.YOUR_UPKEEP_CONDITION],
        new MagicActivationHints(MagicTiming.None),
        "Life+4"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +3/+3 and gains trample until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,3,3));
					game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Trample));
                }
            });
        }
    }
]
