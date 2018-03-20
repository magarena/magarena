[
    new AtUpkeepTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPlayer player) {
            return permanent.isController(player);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                this,
                player.hasState(MagicPlayerState.CitysBlessing) ?
                    "PN draws a card." :
                    "Each player draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new DrawAction(player));
            if (!player.hasState(MagicPlayerState.CitysBlessing))
                game.doAction(new DrawAction(player.getOpponent()));
        }
    }
]


