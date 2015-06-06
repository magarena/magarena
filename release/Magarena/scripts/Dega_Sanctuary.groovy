[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(MagicColor.Black) || 
                upkeepPlayer.controlsPermanent(MagicColor.Red) ? 
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 2 life. If PN controls a black permanent and a red permanent, PN gains 4 life instead."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.controlsPermanent(MagicColor.Black) && player.controlsPermanent(MagicColor.Red)) {
                game.doAction(new ChangeLifeAction(player, 4));
            } else if (player.controlsPermanent(MagicColor.Black) || player.controlsPermanent(MagicColor.Red)) {
                game.doAction(new ChangeLifeAction(player, 2));
            }
        }
    }
]
