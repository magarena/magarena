[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.isCreature() &&
                    otherPermanent.hasSubType(MagicSubType.Goblin)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN sacrifices RN. If PN does, RN deals 4 damage to each creature blocking it."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent goblin = event.getRefPermanent();
            final SacrificeAction sac = new SacrificeAction(goblin);
            final MagicPermanentList blockingCreatures = goblin.getBlockingCreatures();
            game.doAction(sac);
            if (sac.isValid()) {
                for (final MagicPermanent blocker : blockingCreatures) {
                    game.doAction(new DealDamageAction(goblin, blocker, 4));
                }
            }
        }
    }
]
