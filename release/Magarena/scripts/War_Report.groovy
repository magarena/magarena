[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains life equal to the number of creatures on the battlefield "+
                "plus the number of artifacts on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = ARTIFACT.filter(event).size() + CREATURE.filter(event).size();
            game.logAppendMessage(event.getPlayer(), "("+amount+")");
            game.doAction(new ChangeLifeAction(event.getPlayer(), amount));
        }
    }
]
