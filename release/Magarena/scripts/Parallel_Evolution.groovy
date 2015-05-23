[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "For each creature token on the battlefield, its controller puts a token that's a copy of that creature onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPermanent permanent : CREATURE_TOKEN.filter(event)) {
                game.doAction(new PlayTokenAction(permanent.getController(), permanent));
            }
        }
    }
]
