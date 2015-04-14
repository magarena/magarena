[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            final MagicPermanent blocked = blocker.getBlockedCreature();
            final MagicPermanent target = equippedCreature == blocker ? blocker.getBlockedCreature() : blocker;
            final boolean isInvolved = equippedCreature == blocker || equippedCreature == blocked
            return (isInvolved && target.isValid()) ?
                new MagicEvent(
                    permanent,
                    target,
                    this,
                    "Destroy RN and equipped creature."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature = event.getRefPermanent();
            game.doAction(new DestroyAction(creature));
            game.doAction(new DestroyAction(event.getPermanent().getEquippedCreature()));
        }
    }
]
