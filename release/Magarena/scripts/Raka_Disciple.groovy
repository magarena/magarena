[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Flying"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source, "{U}")];
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
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Flying));
                }
            });
        }
    }
]
