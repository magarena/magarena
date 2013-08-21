[
    new MagicIfPlayerWouldLoseTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLoseGameAction loseAct) {
            if (permanent.isController(loseAct.getPlayer())) {
                loseAct.setPlayers(MagicPlayer.NONE);
            }
            return MagicEvent.NONE;
        }
    }
]
