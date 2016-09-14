[
    new MagicHandCastActivation(
        new MagicActivationHints(MagicTiming.Token,true),
        "Flash"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{6}{B}{B}")
            ];
        }
    },
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each player returns all creature cards from his or her graveyard to the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.getAPNAP() each {
                final MagicPlayer player ->
                CREATURE_CARD_FROM_GRAVEYARD.filter(player) each {
                    final MagicCard card ->
                    game.doAction(new ReanimateAction(card, player));
                }
            }
        }
    }
]
