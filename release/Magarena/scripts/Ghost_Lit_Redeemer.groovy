[
    new MagicChannelActivation("{1}{W}", new MagicActivationHints(MagicTiming.Removal, true)) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gains 4 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),4));
        }
    }
]
