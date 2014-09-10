[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
          new MagicPayManaCostEvent(source,"{X}{R}"),
            new MagicDiscardEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "Destroy all artifacts with converted mana cost RN or less."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(
                    new MagicCMCPermanentFilter(
                        MagicTargetFilterFactory.ARTIFACT,
                        Operator.LESS_THAN_OR_EQUAL,
                        event.getRefInt()
                    )
                );
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
