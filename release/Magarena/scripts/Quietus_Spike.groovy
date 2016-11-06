[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent.getEquippedCreature()) && damage.isTargetPlayer() && damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPlayer(),
                    this,
                    "PN loses half his or her life, rounded up."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (player.getLife()>0) {
                game.doAction(new ChangeLifeAction(player,-(player.getLife()+1)/2));
            }
        }
    }
]
