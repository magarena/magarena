[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all tapped creatures. " +
                "PN gains 2 life for each creature destroyed this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final DestroyAction destroy = new DestroyAction(TAPPED_CREATURE.filter(event));
            game.doAction(destroy);
            game.doAction(new ChangeLifeAction(player,destroy.getNumDestroyed()*2));
        }
    }
]
