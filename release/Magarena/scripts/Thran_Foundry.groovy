[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Shuffle"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicTapEvent(source),
                new MagicExileEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_PLAYER,
                this,
                "Target player\$ shuffles his or her graveyard into his or her library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList graveyard = new MagicCardList(it.getGraveyard());
                game.doAction(new ShuffleCardsIntoLibraryAction(graveyard, MagicLocationType.Graveyard))
            });
        }
    }
]
