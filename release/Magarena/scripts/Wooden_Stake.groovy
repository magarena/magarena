[
    new BlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            final MagicPermanent blocked = blocker.getBlockedCreature();
            final MagicPermanent target = equippedCreature == blocker ? blocker.getBlockedCreature() : blocker;
            final boolean isInvolved = equippedCreature == blocker || equippedCreature == blocked
            return (isInvolved && target.isValid() && target.hasSubType(MagicSubType.Vampire)) ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "Destroy RN. It can't be regenerated."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            game.doAction(ChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
            game.doAction(new DestroyAction(creature));
        }
    }
]
