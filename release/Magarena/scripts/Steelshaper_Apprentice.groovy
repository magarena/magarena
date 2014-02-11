[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Equipment"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{W}"),
                new MagicTapEvent(source), 
                new MagicBouncePermanentEvent(source, source),
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches PN's library for an equipment card, reveals it, puts it into PN's hand, and shuffles PN's library."
            );
      }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchIntoHandEvent(
                event,
                MagicTargetChoice.EQUIPMENT_CARD_FROM_LIBRARY
            ));
        }
    }
]
