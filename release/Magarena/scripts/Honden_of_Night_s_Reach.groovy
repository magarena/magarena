[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ? 
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.TARGET_OPPONENT,
                    this,
                    "Target opponent\$ discards a card for each Shrine PN controls."
                ):
                MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicPlayer player ->
                final int size =  event.getPlayer().getNrOfPermanents(MagicSubType.Shrine);
                game.addEvent(MagicDiscardEvent.Random(event.getSource(), player, size));
            });
        }
    }
]
