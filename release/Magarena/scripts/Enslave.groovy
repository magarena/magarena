[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
            return (permanent.isController(upkeepPlayer) && enchantedCreature.isCreature()) ?
                new MagicEvent(
                    enchantedCreature,
                    enchantedCreature.getOwner(),
                    this,
                    "SN deals 1 damage to PN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(
                event.getSource(),
                event.getPlayer(),
                1
            );
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
