[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.isTarget(permanent.getEnchantedPermanent()) ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent().getController(),
                    damage.getDealtAmount(),
                    this,
                    "PN loses RN life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), -event.getRefInt()));
        }
    }
]
