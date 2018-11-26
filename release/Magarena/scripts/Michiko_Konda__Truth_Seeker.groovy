[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return permanent.isEnemy(damage.getSource()) && permanent.isController(damage.getTarget()) ?
                new MagicEvent(
                    permanent,
                    damage.getSource().getController(),
                    this,
                    "PN sacrifices a permanent."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getPermanent(),
                    event.getPlayer(),
                    SACRIFICE_PERMANENT
            ));
        }
    }
]
