[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Land),
        "Search"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source,  "{3}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a basic land card and put that card onto the battlefield tapped. Then shuffle PN's library."
            );
      }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY,
                MagicPlayMod.TAPPED
            ));
        }
    }
]
