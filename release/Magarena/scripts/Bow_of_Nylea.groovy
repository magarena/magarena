[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Return"
    ){
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicPayManaCostEvent(source,"{1}{G}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicFromCardListChoice(source.getController().getGraveyard(),4,true),
                this,
                "PN puts up to 4 target cards from his or her graveyard on the bottom of his or her library. "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, {
                game.doAction(new RemoveCardAction(it,MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(it,MagicLocationType.Graveyard,MagicLocationType.BottomOfOwnersLibrary));
            }); 
        }
    }
]
