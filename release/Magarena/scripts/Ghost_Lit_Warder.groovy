[
    new MagicChannelActivation("{3}{U}", new MagicActivationHints(MagicTiming.Counter, true)) {
        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ unless its controller pays {4}."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicCardOnStack targetSpell ->
                game.addEvent(new MagicCounterUnlessEvent(event.getSource(),targetSpell,MagicManaCost.create("{4}")));
            });
        }
    }
]
