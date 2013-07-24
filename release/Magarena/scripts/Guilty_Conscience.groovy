[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
            final int amount = damage.getAmount();
            return damage.getSource() == enchantedCreature ?
                new MagicEvent(
                    permanent,
                    enchantedCreature,
                    {
                        final MagicGame G, final MagicEvent E ->
                        G.doAction(new MagicDealDamageAction(
                            new MagicDamage(
                                E.getSource(),
                                E.getRefPermanent(),
                                amount
                            )
                        ));
                    } as MagicEventAction,
                    "SN deals ${amount} damage to RN."
                ):
                MagicEvent.NONE;
        }
    }
]
