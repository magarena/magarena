[
    new MagicChannelActivation("{U}", new MagicActivationHints(MagicTiming.Pump, true)) {
        @Override
        public MagicEvent getCardEvent(final MagicSource source, final MagicPayedCost payedCost) {
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
