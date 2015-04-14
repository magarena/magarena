[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (damage.getTarget() == enchantedCreature) ?
                new MagicEvent(
                    enchantedCreature,
                    amount,
                    this,
                    "SN deals RN damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),event.getRefInt()));
        }
    }
]
