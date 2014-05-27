[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final MagicTarget target = damage.getTarget();
            if (player == target && player.controlsPermanent(MagicType.Creature) && player.getLife() < 1) {
                player.setLife(1);
                game.logAppendMessage(player,""+permanent.getName()+" prevented any further damage.");
            }
            return MagicEvent.NONE;
        }
    }
]
