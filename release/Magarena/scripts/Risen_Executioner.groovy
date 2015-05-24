[
    new MagicGraveyardActivation(
        [MagicCondition.SORCERY_CONDITION],
        new MagicActivationHints(MagicTiming.Main),
        "Cast"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final int amount = CREATURE_CARD_FROM_GRAVEYARD.except(source).filter(source.getController()).size() + 2;
            return [
                new MagicPayManaCostEvent(source, "{"+amount+"}{B}{B}")
            ];
        }

        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Play SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReanimateAction(event.getCard(), event.getPlayer()));
        }
    }
]
