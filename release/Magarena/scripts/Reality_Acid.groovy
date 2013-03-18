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
                    enchantedPermanent.getController() + " sacrifices RN."
                ) :
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicSacrificeAction(event.getRefPermanent()));
        }
    }
]
