[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "Put target artifact or creature card from a graveyard\$ onto the battlefield under PN's control. "+
                "Shuffle SN into its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new MagicReanimateAction(
                    it,
                    event.getPlayer()
                ));
                game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersLibrary));
            });
        }
    }
]
