[
    new BlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            final MagicPermanent blocked = blocker.getBlockedCreature();
            final MagicPermanent target = equippedCreature == blocker ? blocker.getBlockedCreature() : blocker;
            final boolean isInvolved = equippedCreature == blocker || equippedCreature == blocked
            return (isInvolved && target.isValid()) ?
                new MagicEvent(
                    permanent,
                    new MagicPermanentList(equippedCreature, target),
                    this,
                    String.format(
                        "Destroy %s and %s.",
                        MagicMessage.getCardToken(equippedCreature),
                        MagicMessage.getCardToken(target)
                    )
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DestroyAction(event.getRefPermanentList()));
        }
    }
]
