[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.ExileOpp,
                this,
                "Exile target card\$ from a graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.Graveyard
                    ));
                    game.doAction(new MagicMoveCardAction(
                        card,
                        MagicLocationType.Graveyard,
                        MagicLocationType.Exile
                    ));
                }
            });
        }
    }
]
