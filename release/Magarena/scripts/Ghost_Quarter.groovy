[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target land\$. Its controller may search his or her library for a basic land card, put it onto the battlefield, then shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice(
                        "Search for a basic land card?",
                        A_BASIC_LAND_CARD_FROM_LIBRARY
                    )
                ));
            });
        }
    }
]
