[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_PLAYER,
                this,
                "Exile all creature cards from target player's\$ graveyard, "+
                "then create a 2/2 black Zombie creature token for each card exiled this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                for (final MagicCard card : new MagicCardList(CREATURE_CARD_FROM_GRAVEYARD.filter(it))) {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
                    if (card.isInExile()) {
                    game.doAction(new PlayTokensAction(event.getPlayer(), CardDefinitions.getToken("2/2 black Zombie creature token"), 1));
                  }
               }
           });
        }
    }
]
