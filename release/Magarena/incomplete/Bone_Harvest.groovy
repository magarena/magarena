[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicCardList bones = new MagicCardList();
            for (final MagicCard card : cardOnStack.getController().getGraveyard()) {
                if (card.hasType(MagicType.Creature)) {
                    bones.add(card);
                }
            }
            return new MagicEvent(
                cardOnStack,
                new MagicFromCardListChoice(bones, bones.size(), true),
                this,
                "PN puts any number of target creature cards\$ from his or her graveyard on top of his or her library."+
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
            game.doAction(new MagicAddTriggerAction(
                MagicAtUpkeepTrigger.YouDraw(
                    event.getSource(), 
                    event.getPlayer()
                )
            ));
        }
    }
]
