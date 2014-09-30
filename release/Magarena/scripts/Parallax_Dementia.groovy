[
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicRemoveFromPlayAction act) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedPermanent();
                new MagicEvent(
                    permanent,
                    enchantedPermanent,
                    this,
                    "Destroy RN."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(MagicChangeStateAction.Set(event.getRefPermanent(),MagicPermanentState.CannotBeRegenerated));
            game.doAction(new MagicDestroyAction(event.getRefPermanent()));
        }
    }
]
