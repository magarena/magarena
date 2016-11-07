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
            game.doAction(new ChangeLifeAction(player,-player.getHalfLifeRoundUp()));
        }
    }
]
