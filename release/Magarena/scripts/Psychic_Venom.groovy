[
    new BecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchantedLand = permanent.getEnchantedPermanent();
            return (enchantedLand.isLand() && enchantedLand==tapped) ?
                new MagicEvent(
                    permanent,
                    tapped.getController(),
                    this,
                    "SN deals 2 damage to PN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
        }
    }
]
