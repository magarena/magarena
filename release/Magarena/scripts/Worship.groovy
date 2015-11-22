[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final MagicTarget target = damage.getTarget();
            if (player == target && player.controlsPermanent(MagicType.Creature) && player.getLife() < 1) {
                player.setLife(1);
            }
            return MagicEvent.NONE;
        }
    }
]
