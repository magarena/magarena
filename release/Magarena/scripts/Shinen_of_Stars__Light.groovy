[
    new MagicChannelActivation("{1}{W}", new MagicActivationHints(MagicTiming.Pump, true)) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicFirstStrikeTargetPicker.create(),
                this,
                "Target creature\$ gains first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final MagicPermanent creature ->
                game.doAction(new MagicGainAbilityAction(creature,MagicAbility.FirstStrike));
            });
        }
    }
]
