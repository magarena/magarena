[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "-Life"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each player loses 1 life for each swamp he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new ChangeLifeAction(
                    player,
                    -1 * player.getNrOfPermanents(MagicSubType.Swamp)
                ));
            }
        }
    }
]
