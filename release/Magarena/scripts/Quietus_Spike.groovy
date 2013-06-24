[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicTarget target=damage.getTarget();
            return (permanent.getEquippedCreature()==damage.getSource() &&
                    target.isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getTargetPlayer(),
                    this,
                    "You lose half your life, rounded up."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player,-(player.getLife()+1)/2));
        }
    }
]
