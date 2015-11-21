[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchantedCreature=permanent.getEnchantedPermanent();
            return (enchantedCreature.isValid() && enchantedCreature.isUntapped()) ?
                new MagicEvent(
                    permanent,
                    enchantedCreature,
                    this,
                    "If RN is untapped, tap it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent enchantedCreature = event.getRefPermanent();
            if (enchantedCreature.isUntapped()) {
                game.doAction(new TapAction(enchantedCreature));
            }
        }
    }
]
