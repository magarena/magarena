[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return new MagicEvent(
                permanent,
                permanent == blocker ? blocker.getBlockedCreature() : blocker,
                this,
                "Destroy RN at end of combat."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicAddTriggerAction(
                    permanent,
                    MagicAtEndOfCombatTrigger.Destroy
                ))
            } as MagicPermanentAction);
        }
    }
]
