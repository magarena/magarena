[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Land),
        "Land"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicSacrificePermanentEvent(
                    source,
                    MagicTargetChoice.SACRIFICE_CREATURE
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a land card with basic land type and put that card onto the battlefield tapped. Then shuffle PN's library."
            );
      }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                MagicTargetChoice.LAND_CARD_WITH_BASIC_LAND_TYPE_FROM_LIBRARY,
                MagicPlayMod.TAPPED
            ));
        }
    }
]
