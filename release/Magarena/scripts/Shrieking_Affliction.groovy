[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return (permanent.isOpponent(player) &&
                    player.getHandSize() <= 1) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "PN loses 3 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(),-3));
        }
    }
]
