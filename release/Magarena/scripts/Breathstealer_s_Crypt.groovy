[
    new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            card.reveal();
            return card.hasType(MagicType.Creature) ? new MagicEvent(
                card,
                card.getOwner(),
                new MagicMayChoice(),
                1,
                this,
                "PN draws "+card.getName()+". He or she discards it unless he or she pays 3 life\$."
            ): new MagicEvent(
                card,
                card.getOwner(),
                0,
                this,
                "PN draws "+card.getName()+"."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefInt() == 1) {
                if (event.isYes()) { 
                    game.addEvent(new MagicPayLifeEvent(event.getSource(),event.getPlayer(),3));
                } else {
                    game.doAction(new MagicRemoveCardAction(event.getCard(),MagicLocationType.OwnersHand));
                    if (event.getPlayer() == event.getSource().getController()) {
                        game.doAction(new MagicMoveCardAction(event.getCard(),MagicLocationType.OwnersHand,MagicLocationType.Graveyard));
                    } else {
                        game.doAction(new MagicMoveCardAction(event.getCard(),MagicLocationType.OwnersHand,MagicLocationType.OpponentsGraveyard));
                    }
                }
            }
        }
    }
]
