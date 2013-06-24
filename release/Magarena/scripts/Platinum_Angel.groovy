[
    new MagicIfPlayerWouldLoseTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer[] playerRef) {
            final MagicPlayer controller = permanent.getController();
            if (controller == playerRef[0]) {
                playerRef[0] = MagicPlayer.NONE;
            }
            return MagicEvent.NONE;
        }
    }
]
