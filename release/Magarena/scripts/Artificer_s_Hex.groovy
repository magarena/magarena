[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) && permanent.getEnchantedPermanent().getEquippedCreature().isValid() ?
                new MagicEvent(
                    permanent,
                    this,
                    "Destroy equipped creature. ("+permanent.getEnchantedPermanent().getEquippedCreature()+")"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPermanent().getEnchantedPermanent().getEquippedCreature().isValid()) {
                game.doAction(new MagicDestroyAction(event.getPermanent().getEnchantedPermanent().getEquippedCreature()));
            }
        }
    }
]
