[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(MagicColor.White) || 
                upkeepPlayer.controlsPermanent(MagicColor.Blue) ? 
                new MagicEvent(
                    permanent,
                    NEG_TARGET_CREATURE,
                    new MagicDamageTargetPicker(1),
                    this,
                    "SN deals 1 damage to target creature.\$ If PN controls a white permanent "+
                    "and a blue permanent, SN deals 3 damage to that creature instead."
                ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                final MagicPermanent permanent = event.getPermanent();
                if (player.controlsPermanent(MagicColor.White) && player.controlsPermanent(MagicColor.Blue)) {
                    game.doAction(new DealDamageAction(permanent, it, 3));
                } else if (player.controlsPermanent(MagicColor.Green) || player.controlsPermanent(MagicColor.White)) {
                    game.doAction(new DealDamageAction(permanent, it, 1));
                }
            });
        }
    }
]
