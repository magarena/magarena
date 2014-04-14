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
            return new MagicEvent(
                cardOnStack,
                new MagicFromCardListChoice(cardOnStack.getController().getLibrary(), 3, false),
                this,
                "PN searches his or her library for three cards, exiles them, then shuffles his or her library..\$"+
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, {
                MagicCard card ->
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
            });
            game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
            game.doAction(new MagicAddCantripTriggerAction(event)); 
        }
    }
]
