[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return (game.getCreatureDiedThisTurn()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Return a creature card at random from your graveyard to your hand."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            new MagicCardList(CREATURE_CARD_FROM_GRAVEYARD.filter(event)).getRandomCards(1) each {
                game.doAction(new RemoveCardAction(it, MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(it, MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
