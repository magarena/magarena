[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return enchantedCreature == tapped ?
                new MagicEvent(
                    permanent,
                    tapped.getController(),
                    this,
                    "SN deals 3 damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final MagicDamage damage=new MagicDamage(event.getSource(),event.getPlayer(),3);
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
