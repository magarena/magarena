[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            final MagicPermanent blocked = blocker.getBlockedCreature();
            final MagicPermanent target = enchantedCreature == blocker ? blocker.getBlockedCreature() : blocker;
            final boolean isInvolved = enchantedCreature == blocker || enchantedCreature == blocked
            return (isInvolved && target.isValid() && !target.hasSubType(MagicSubType.Wall)) ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "Destroy RN at end of combat."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                game.doAction(new AddTurnTriggerAction(
                    it,
                    AtEndOfCombatTrigger.Destroy
                ))
            });
        }
    }
]
