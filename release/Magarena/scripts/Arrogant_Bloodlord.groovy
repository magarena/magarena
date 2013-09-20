[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return (permanent == blocker && blocked.isValid() && blocker.getPower<2) ?
				new MagicEvent(
                permanent,                
                this,
                "Destroy Arrogant Bloodlord at end of combat."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {          
            game.doAction(new MagicAddTriggerAction(
                event.getPermanent(),
                MagicAtEndOfCombatTrigger.Destroy
            ));			
        }
    }
]
