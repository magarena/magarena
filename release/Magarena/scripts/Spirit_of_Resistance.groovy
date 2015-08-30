[
    new MagicPreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            if (permanent.isController(damage.getTarget()) &&
                player.controlsPermanent(MagicColor.White) &&
                player.controlsPermanent(MagicColor.Blue) &&
                player.controlsPermanent(MagicColor.Black) &&
                player.controlsPermanent(MagicColor.Red) &&
                player.controlsPermanent(MagicColor.Green)
            ) {
                // Prevention effect, All damage.
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    }
]
