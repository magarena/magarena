[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    upkeepPlayer,
                    this,
                    "RN loses 1 life for each card fewer than three in RN's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getRefPlayer();
            final int loss = 3 - player.getHandSize();
            if (loss > 0) {
                game.doAction(new ChangeLifeAction(player, -loss));
            }
        }
    }
]
