[
    new IfPlayerWouldLoseTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final LoseGameAction loseAct) {
            // Exquisite Archangel doesn't apply for "win the game" effects
            // How to differentiate those from "lose the game" effects?
            if (permanent.isController(loseAct.getPlayer())) {
                loseAct.setPlayer(MagicPlayer.NONE);
                return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    this,
                    "Exile SN and PN's life total becomes equal to PN's starting life total."
                );
            }
            else {
                return MagicEvent.NONE;
            }
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.addEvent(new MagicExileSelfEvent(event.getSource(), MagicLocationType.Battlefield));
            game.doAction(new ChangeLifeAction(player, player.getStartingLife() - player.getLife()));
        }
    }
]

