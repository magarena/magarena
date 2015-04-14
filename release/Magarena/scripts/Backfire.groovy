[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (damage.getSource() == enchantedCreature && permanent.isController(damage.getTarget())) ?
                new MagicEvent(
                    permanent,
                    enchantedCreature.getController(),
                    damage.getDealtAmount(),
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
