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
                    ""+enchantedPermanent.getController()+" loses life equal to "+enchantedPermanent.getName()+"'s toughness ("+enchantedPermanent.getToughness()+")."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int toughness = event.getRefPermanent().getToughness();
            game.doAction(new MagicChangeLifeAction(event.getRefPermanent().getController(),-toughness));
        }
    }
]
