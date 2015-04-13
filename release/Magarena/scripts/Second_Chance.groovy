[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getLife() <= 5 ? 
                new MagicEvent(
                    permanent,
                    this,
                    "PN sacrifices SN. PN takes an extra turn after this one."
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.getLife() <= 5) {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                game.doAction(new ChangeExtraTurnsAction(player,1));
            }
        }
    }
]
