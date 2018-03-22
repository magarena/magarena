def sac = new MagicRegularCostEvent("Sacrifice three Clerics");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Search"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                sac.getEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her graveyard, hand, and/or library a card named Scion of Darkness and put it onto the battlefield. " +
                "Shuffle PN's library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter filter =
                MagicTargetFilterFactory.cardName("Scion of Darkness")
                .from(MagicTargetType.Graveyard)
                .from(MagicTargetType.Hand)
                .from(MagicTargetType.Library);
            final MagicTargetChoice choice = new MagicTargetChoice(
                filter,
                "a card name Scion of Darkness from your graveyard, hand, and/or library"
            );
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                choice
            ));
        }
    }
]
