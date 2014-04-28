[
    new MagicPermanentActivation(new MagicActivationHints(MagicTiming.Main),"Return"){
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
                this,
                "Put up to 4 target cards from your graveyard on the bottom of your library. "
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList choiceList = new MagicCardList();
            final int size = event.getPlayer().getGraveyard().size();
            final int amount = (size > 3) ? 4 : size;
            if (size > 1) {
                choiceList.setCards(event.getPlayer().getGraveyard());
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList,amount,true),
                    {
                        final MagicGame game2, final MagicEvent event2 ->
                        event2.processChosenCards(game2, {
                            final MagicCard card ->
                            game2.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                            game2.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.BottomOfOwnersLibrary));
                        }); 
                    },
                    ""
                )); 
            }
        }
    }
]
