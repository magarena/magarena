[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(MagicColor.Blue) || 
                upkeepPlayer.controlsPermanent(MagicColor.Black) ? 
                new MagicEvent(
                    permanent,
                    POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.create(),
                    this,
                    "Target creature\$ gets +1/+1 until end of turn. If PN controls a blue permanent "+
                    "and a black permanent, that creature gets +5/+5 until end of turn instead."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                if (player.controlsPermanent(MagicColor.Black) && player.controlsPermanent(MagicColor.Blue)) {
                    game.doAction(new ChangeTurnPTAction(it, 5, 5));
                } else if (player.controlsPermanent(MagicColor.Black) || player.controlsPermanent(MagicColor.Blue)) {
                    game.doAction(new ChangeTurnPTAction(it, 1, 1));
                }
            });
        }
    }
]
