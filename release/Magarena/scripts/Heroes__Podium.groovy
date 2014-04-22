def LEGENDARY_CREATURE_YOU_CONTROL = MagicTargetFilterFactory.multiple("legendary creatures you control");

def MoveCards = {
    final MagicGame game, final MagicEvent event ->
    final List<MagicCard> putBackList = new MagicCardList();
    putBackList.addAll(event.getRefCardList());
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.OwnersHand));
        putBackList.remove(card);
    });
    putBackList.shuffle();
    for(final MagicCard card : putBackList) {
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.BottomOfOwnersLibrary));
    }
}

[ 
    new MagicStatic(
        MagicLayer.ModPT,
        LEGENDARY_CREATURE_YOU_CONTROL
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = source.getController().getNrOfPermanents(LEGENDARY_CREATURE_YOU_CONTROL) - 1;
            pt.add(amount,amount); 
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicPayManaCostEvent(source, "{X}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "PN looks at the top RN cards of his or her library. "+
                "PN may reveal a legendary creature card from among them and put it into his or her hand."+
                "Then he or she puts the rest on the bottom of his or her library in a random order."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> showList = event.getPlayer().getLibrary().getCardsFromTop(event.getRefInt());
            final List<MagicCard> choiceList = new MagicCardList();
            for (final MagicCard card : showList) {
                if (card.hasType(MagicType.Legendary) && card.hasType(MagicType.Creature)) {
                    choiceList.add(card);
                }
            }
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(choiceList, showList, 1, true),
                showList,
                MoveCards,
                ""
            ));
        }
    }
]
