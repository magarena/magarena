[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (damage.getTarget() == permanent.getEnchantedPermanent()) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "SN deals RN damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getPlayer(),
                event.getRefInt()
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
