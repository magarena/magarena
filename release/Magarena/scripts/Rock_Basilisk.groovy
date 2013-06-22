[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent target = permanent == blocker ? blocker.getBlockedCreature() : blocker;
            return !target.hasSubType(MagicSubType.Wall) ?
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
            game.doAction(MagicChangeStateAction.Set(
                event.getRefPermanent(),
                MagicPermanentState.DestroyAtEndOfCombat
            ));
        }
    }
]
