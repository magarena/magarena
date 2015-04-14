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
            final MagicPlayer player = event.getPlayer();
            final MagicCardList cards = new MagicCardList(game.filterCards(player,ZOMBIE_CARD_FROM_GRAVEYARD));
            for (final MagicCard card : cards.getRandomCards(1)) {
                game.doAction(new RemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
