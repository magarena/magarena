[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(MagicColor.Green) ||
                upkeepPlayer.controlsPermanent(MagicColor.White) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_PLAYER,
                    this,
                    "Target player\$ loses 1 life. If PN controls a green permanent and "+
                    "a white permanent, that player loses 3 life instead."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player = event.getPlayer();
                if (player.controlsPermanent(MagicColor.Green) && player.controlsPermanent(MagicColor.White)) {
                    game.doAction(new ChangeLifeAction(it, -3));
                } else if (player.controlsPermanent(MagicColor.Green) || player.controlsPermanent(MagicColor.White)) {
                    game.doAction(new ChangeLifeAction(it, -1));
                }
            });
        }
    }
]
