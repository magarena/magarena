[
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent left) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedCreature();
            return (permanent == left && enchantedPermanent != MagicPermanent.NONE) ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent,
                    this,
                    "RN's controller sacrifices it."
                ) :
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicSacrificeAction(event.getRefPermanent()));
        }
    }
]
