def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicTuple tup = event.getRefTuple();
    game.doAction(new DealDamageAction(
        event.getSource(),
        tup.getPermanent(1),
        tup.getInt(0)
    ));
}

[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            final int amount = damage.getDealtAmount();
            return damage.isSource(enchantedCreature) ?
                new MagicEvent(
                    permanent,
                    new MagicTuple(amount, enchantedCreature),
                    action,
                    "SN deals ${amount} damage to ${enchantedCreature}."
                ):
                MagicEvent.NONE;
        }
    }
]
