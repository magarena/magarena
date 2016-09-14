
[
    new MagicPermanentActivation(
        [MagicCondition.HAS_EXILED_CARD],
        new MagicActivationHints(MagicTiming.Removal),
        "Copy"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,  "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getExiledCard(),
                this,
                "PN cast a copy of RN without paying its mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(CastCardAction.WithoutManaCost(
                event.getPlayer(),
                MagicCard.createTokenCard(event.getRefCard(), event.getPlayer()),
                MagicLocationType.Exile,
                MagicLocationType.Graveyard
            ));
        }
    }
]
