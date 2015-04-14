[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN chooses a card at random in his or her graveyard. " +
                "If it's a creature card, put it onto the battlefield. " +
                "Otherwise, put it into PN's hand."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            for (final MagicCard card : player.getGraveyard().getRandomCards(1)) {
                if (card.hasType(MagicType.Creature)) {
                    game.doAction(new ReanimateAction(
                        card,
                        player
                    ));
                } else {
                    game.doAction(new RemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                    game.logAppendMessage(player, "${player.getName()} returns (${card.getName()}) to his or her hand.");
                }
            }
        }
    }
]
