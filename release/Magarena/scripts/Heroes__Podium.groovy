def LEGENDARY = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) && target.hasType(MagicType.Legendary) && target.isCreature();
    }
};

def EventAction = {
    final MagicGame game, final MagicEvent event ->
    final List<MagicCard> putBackList = new MagicCardList();
    putBackList.addAll(event.getRefCardList());
    event.processChosenCards(game, {
        final MagicCard card ->
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.OwnersHand));
        putBackList.remove(card);
    });
    if(putBackList.size() > 0) {
        putBackList.shuffle();
        for(final MagicCard card : putBackList) {
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.BottomOfOwnersLibrary));
        }
    }
}

[ 
    new MagicStatic(
        MagicLayer.ModPT,
        LEGENDARY
    ) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = source.getController().getNrOfPermanents(LEGENDARY) - 1;
            pt.add(amount,amount); 
        }
    },
    
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{X}")];
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
            
            final List<MagicCard> showList = new MagicCardList();
            final List<MagicCard> choiceList = new MagicCardList();
            final int amount = (event.getPlayer().getLibrary().size() >= event.getRefInt()) ? event.getRefInt() : event.getPlayer().getLibrary().size();
            if(amount > 0) {
                showList.addAll(event.getPlayer().getLibrary().getCardsFromTop(amount));
                for(final MagicCard card : showList) {
                    if(card.hasType(MagicType.Legendary) && card.hasType(MagicType.Creature)) choiceList.add(card);
                }
            }
            if(choiceList.size() > 0) {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    new MagicFromCardListChoice(choiceList, showList, 1, true),
                    showList,
                    EventAction,
                    ""
                ));
            }
        }            
    }
]
