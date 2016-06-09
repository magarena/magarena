[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "For each token PN controls, he or she puts a token onto the battlefield that's a copy of that permanent."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            TOKEN_YOU_CONTROL.filter(event) each {
                game.doAction(new PlayTokenAction(event.getPlayer(), it));
            }
        }
    }
]
