[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return enchantedCreature.isCreature() ?
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
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),1));
        }
    }
]
