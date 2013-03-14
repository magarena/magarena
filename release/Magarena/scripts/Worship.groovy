[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final MagicTarget target = damage.getTarget();
            if (player == target && player.getNrOfPermanentsWithType(MagicType.Creature) > 0 && player.getLife() < 1) {
                player.setLife(1);
            }
            return MagicEvent.NONE;
        }
    }
]
