[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (permanent.isController(upkeepPlayer) && upkeepPlayer.getHandSize() < 7) ? 
                new MagicEvent(
                    permanent,
                    this,
                    "If PN has fewer than seven cards in hand, draw cards equal to the difference."
                ):
                MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = 7 - player.getHandSize();
            if (amount > 0) {
                game.doAction(new MagicDrawAction(event.getPlayer(),amount));
            }
        }
    }
]
