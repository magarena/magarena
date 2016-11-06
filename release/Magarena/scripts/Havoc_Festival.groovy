[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN loses half his or her life, rounded up."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.getLife>0) {
                game.doAction(new ChangeLifeAction(player,-(player.getLife()+1)/2));
            }
        }
    }
]
