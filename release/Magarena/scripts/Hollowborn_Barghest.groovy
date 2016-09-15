[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return (permanent.isOpponent(player) && player.getHandSize() <= 0) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "If PN has no cards in hand, PN loses 2 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getHandSize() == 0) {
                game.doAction(new ChangeLifeAction(event.getPlayer(),-2));
            }
        }
    }
]
