[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Reanimate"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicExileEvent(source),
                new MagicPayManaCostEvent(source,"{B}{B}{B}")

            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each player returns all black and all red creature cards from his or her graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                BLACK_OR_RED_CREATURE_CARD_FROM_GRAVEYARD.filter(player) each {
                    game.doAction(new ReanimateAction(it, player));
                }
            }
        }
    }
]
