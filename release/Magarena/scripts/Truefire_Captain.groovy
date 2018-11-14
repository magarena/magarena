[
    new DamageIsDealtTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            return damage.getTarget() == permanent;
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_PLAYER,
                damage.getDealtAmount(),
                this,
                "SN deals RN damage to target player.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, event.getRefInt()));
            });
        }
    }
]
