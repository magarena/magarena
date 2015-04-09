[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains X + 1 life, where X is the number of green creatures on the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = game.getNrOfPermanents(GREEN_CREATURE);
            game.doAction(new MagicChangeLifeAction(event.getPlayer(), X + 1));
        }
    }
]
