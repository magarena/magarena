[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            final int amount = damage.getDealtAmount();
            return damage.isSource(enchantedCreature) ?
                new MagicEvent(
                    permanent,
                    enchantedCreature,
                    {
                        final MagicGame G, final MagicEvent E ->
                        G.doAction(new MagicDealDamageAction(E.getSource(),E.getRefPermanent(),amount));
                    },
                    "SN deals ${amount} damage to RN."
                ):
                MagicEvent.NONE;
        }
    }
]
