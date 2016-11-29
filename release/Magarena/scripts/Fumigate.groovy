[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all creatures. " +
                "PN gains 1 life for each creature destroyed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final DestroyAction destroy = new DestroyAction(CREATURE.filter(event));
            game.doAction(destroy);
            game.doAction(new ChangeLifeAction(event.getPlayer(), destroy.getNumDestroyed()));
        }
    }
]
