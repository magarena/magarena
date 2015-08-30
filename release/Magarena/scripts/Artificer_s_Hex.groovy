[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.getEnchantedPermanent().getEquippedCreature().isValid() ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent().getEquippedCreature(),
                    this,
                    "Destroy RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getRefPermanent()));
        }
    }
]
