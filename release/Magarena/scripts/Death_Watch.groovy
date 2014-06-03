[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            final int toughness = enchantedPermanent.getToughness();
            final int power = enchantedPermanent.getPower();
            return enchantedPermanent == died ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent,
                    this,
                    ""+enchantedPermanent.getController()+" loses life equal to "+enchantedPermanent.getName()+"'s power ("+power+"). "+
                    "PN gains life equal to "+enchantedPermanent.getName()+"'s toughness ("+toughness+")."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent enchantedPermanent = event.getRefPermanent();
            final int toughness = enchantedPermanent.getToughness();
            final int power = enchantedPermanent.getPower();
            final MagicPlayer controller = enchantedPermanent.getController();
            game.doAction(new MagicChangeLifeAction(event.getPlayer(), toughness));
            game.doAction(new MagicChangeLifeAction(controller, -power)); 
        }
    }
]
