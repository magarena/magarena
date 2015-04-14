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
            final Collection<MagicPermanent> targets = game.filterPermanents(CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(ChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
            }
            final DestroyAction destroy = new DestroyAction(targets);         
            game.doAction(destroy); 
            game.doAction(new DrawAction(event.getPlayer(),destroy.getNumDestroyed()));
        }
    }
]
