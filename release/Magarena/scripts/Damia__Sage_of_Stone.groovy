[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return (permanent.getController() == upkeepPlayer && permanent.getController().getHandSize()<7) ? new MagicEvent(
                permanent,
                permanent.getController(),
                this,
                "if PN have fewer than seven cards in hand, draw cards equal to the difference."
            ):
            MagicEvent.NONE
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = 7 - player.getHandSize() ;            
            game.doAction(new MagicDrawAction(event.getPlayer(),amount));
        }
    }
]
