[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_LAND,
                new MagicDestroyTargetPicker(false),
                this,
                "Destroy target land\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicDestroyAction(permanent));
                    game.addEvent(new MagicSearchOntoBattlefieldEvent(
                        event.getSource(),
                        permanent.getController(),
                        new MagicMayChoice(
                            "Search for a basic land card?",
                            MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
                        )
                    ));
                }
            });
        }
    }
]
