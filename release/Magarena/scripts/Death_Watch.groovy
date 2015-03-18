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
                    "${enchantedPermanent.getController()} loses life equal to RN's power (${enchantedPermanent.getPower()}). "+
                    "PN gains life equal to RN's toughness (${enchantedPermanent.getToughness()})."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent enchantedPermanent = event.getRefPermanent();
            game.doAction(new MagicChangeLifeAction(
                enchantedPermanent.getController(), 
                -enchantedPermanent.getPower()
            )); 
            game.doAction(new MagicChangeLifeAction(
                event.getPlayer(),
                enchantedPermanent.getToughness()
            ));
        }
    }
]
