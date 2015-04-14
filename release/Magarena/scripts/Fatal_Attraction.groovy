[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return enchantedCreature.isValid() ?
                new MagicEvent(
                    permanent,
                    enchantedCreature,
                    this,
                    "SN deals 2 damage to RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getPermanent(),event.getRefPermanent(),2));
        }
    },
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return enchantedCreature.isValid() ?
                new MagicEvent(
                    permanent,
                    enchantedCreature,
                    this,
                    "SN deals 4 damage to RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getPermanent(),event.getRefPermanent(),4));
        }
    }
]
