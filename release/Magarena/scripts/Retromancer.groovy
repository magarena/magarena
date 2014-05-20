[
    new MagicWhenSelfTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return new MagicEvent(
                permanent,
                target.getController(),
                this,
                "SN deals 3 damage to RN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(event.getPermanent(), event.getRefPlayer(), 3);
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
