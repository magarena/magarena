[
    new IfPlayerWouldLoseTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final LoseGameAction loseAct) {
            if (permanent.isController(loseAct.getPlayer())) {
                loseAct.setPlayer(MagicPlayer.NONE);
                return new MagicEvent(
                    permanent,
                    this,
                    "Exile SN and PN's life total becomes equal to PN's starting life total."
                );
            } else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new RemoveFromPlayAction(event.getPermanent(), MagicLocationType.Exile));
            game.doAction(new ChangeLifeAction(player, player.getStartingLife() - player.getLife()));
        }
    }
]

