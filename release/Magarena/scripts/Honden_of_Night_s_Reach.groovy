[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ discards a card for each Shrine PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int size = event.getPlayer().getNrOfPermanents(MagicSubType.Shrine);
                game.addEvent(new MagicDiscardEvent(event.getSource(), it, size));
            });
        }
    }
]
