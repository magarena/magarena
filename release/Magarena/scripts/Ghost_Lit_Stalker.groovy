[
    new MagicChannelActivation([MagicCondition.SORCERY_CONDITION],"{5}{B}{B}", new MagicActivationHints(MagicTiming.Main, true)) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player discards four cards."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,{
                final MagicPlayer player ->
                game.addEvent(new MagicDiscardEvent(event.getSource(),player,4));
            });
        }
    }
]
