[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return enchantedPermanent == died ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent,
                    this,
                    "SN deals damage equal to "+enchantedPermanent.getName()+"'s toughness ("+enchantedPermanent.getToughness()+") to "+enchantedPermanent.getController()
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getRefPermanent();
            game.doAction(new DealDamageAction(event.getSource(),permanent.getController(),permanent.getToughness()));
        }
    }
]
