[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN returns a Zombie card at random from his or her graveyard to his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            new MagicCardList(ZOMBIE_CARD_FROM_GRAVEYARD.filter(event)).getRandomCards(1) each {
                game.doAction(new RemoveCardAction(it, MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(it, MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
