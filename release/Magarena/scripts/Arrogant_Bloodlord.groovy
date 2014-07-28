[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent creature = permanent == blocker ? blocker.getBlockedCreature() : blocker;
            return (creature.getPower() <= 1) ?
                new MagicEvent(
                    permanent,                
                    this,
                    "Destroy Arrogant Bloodlord at end of combat."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {          
            event.processPermanent(game, {
                game.doAction(new MagicAddTurnTriggerAction(
                    it,
                    MagicAtEndOfCombatTrigger.Destroy
                ))
            });
        }
    }
]
