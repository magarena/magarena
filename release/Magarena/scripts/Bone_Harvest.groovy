def MagicAtUpkeepTrigger cantrip(final MagicEvent event) {
    return new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                {
                    final MagicGame game2, final MagicEvent event2 ->
                    game2.doAction(new MagicDrawAction(event2.getPlayer()));
                    game2.doAction(new MagicRemoveTriggerAction(this));
                },
                "PN draws a card"
            );
        }
    }
}; 


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicCardList bones = new MagicCardList();
            for(int i = 0; i < cardOnStack.getController().getGraveyard().size(); i++) {
                //if(cardOnStack.getController().getGraveyard().get(i).hasType(MagicType.Creature)) {
                    bones.add(cardOnStack.getController().getGraveyard().get(i));
               // }
            }
            return new MagicEvent(
                cardOnStack,
                new MagicFromCardListChoice(bones, bones.size(), true),
                this,
                "PN puts any number of target creature cards from his or her graveyard on top of his or her library.\$"+
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, {
                MagicCard card ->
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.TopOfOwnersLibrary));
            });
            game.doAction(new MagicAddTriggerAction(cantrip(event))); 
        }
    }
]
