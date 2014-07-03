[
    new MagicChannelActivation("{3}{G}", new MagicActivationHints(MagicTiming.Pump, true)) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +4/+4 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final MagicPermanent creature ->
                game.doAction(new MagicChangeTurnPTAction(creature,4,4));
            });
        }
    }
]
