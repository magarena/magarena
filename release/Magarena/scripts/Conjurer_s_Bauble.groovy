[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Card"
    ){
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicSacrificeEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CARD_FROM_GRAVEYARD,
                MagicGraveyardTargetPicker.ExileOpp,
                this,
                "Put target card\$ from your graveyard on the bottom of your library"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new MagicRemoveCardAction(it,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(
                    it,
                    MagicLocationType.Graveyard,
                    MagicLocationType.BottomOfOwnersLibrary
                ));
                game.doAction(new DrawAction(event.getPlayer()));
            });
        }
    }
]
