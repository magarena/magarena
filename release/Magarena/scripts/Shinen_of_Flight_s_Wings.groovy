[
    new MagicCardActivation(
        [MagicCondition.HAND_CONDITION],
        new MagicActivationHints(MagicTiming.Pump),
        "Channel"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{U}"),
                new MagicDiscardSelfEvent(source)
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicSource source) {
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
            event.processTargetPermanent(game,{
                final MagicPermanent creature ->
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.Flying));
            });
        }
    }
]
