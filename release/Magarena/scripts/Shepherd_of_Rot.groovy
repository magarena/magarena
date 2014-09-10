[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "LifeLoss"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int X = source.getGame().getNrOfPermanents(MagicSubType.Zombie);
            return new MagicEvent(
                source,
                this,
                "Each player loses 1 life for each Zombie on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                final int X = game.getNrOfPermanents(MagicSubType.Zombie);
                for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new MagicChangeLifeAction(player,-X));
            }
        }
    }
]
