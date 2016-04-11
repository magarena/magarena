[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getHandSize() == 0 ? 
                new MagicEvent(
                    permanent,
                    this,
                    "If {upkeepPlayer.getName()} has no cards in hand, PN draws a card and loses 1 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (game.getTurnPlayer().getHandSize()==0) {
                final MagicPlayer player = event.getPlayer();
                game.doAction(new DrawAction(player));
                game.doAction(new ChangeLifeAction(player, -1));
            }
        }
    }
]
