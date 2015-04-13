[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
            return enchantedPermanent == died ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent.getController(),
                    enchantedPermanent,
                    this,
                    "PN loses life equal to RN's toughness (${enchantedPermanent.getToughness()})."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int toughness = event.getRefPermanent().getToughness();
            game.doAction(new ChangeLifeAction(event.getPlayer(),-toughness));
        }
    }
]
