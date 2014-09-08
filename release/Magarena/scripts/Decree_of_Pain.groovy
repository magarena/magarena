[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all creatures. They can't be regenerated. Draw a card for each creature destroyed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                final Collection<MagicPermanent> targets=
                    game.filterPermanents(MagicTargetFilterFactory.CREATURE);
		final MagicDestroyAction destroy = new MagicDestroyAction(targets);         
	        for (final MagicPermanent target : targets) {
                game.doAction(MagicChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
        }
                game.doAction(destroy); 
                game.doAction(new MagicDrawAction(event.getPlayer(),destroy.getNumDestroyed()));
        }
    }
]
