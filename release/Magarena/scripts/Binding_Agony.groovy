[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent.getEnchantedPermanent()) ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent().getController(),
                    amount,
                    this,
                    "SN deals RN damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(
                event.getSource(),
                event.getPlayer(),
                event.getRefInt()
            ));
        }
    }
]
