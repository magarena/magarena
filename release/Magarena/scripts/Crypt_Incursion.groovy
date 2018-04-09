[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Exile all creature cards from target player's\$ graveyard. "+
                "PN gain 3 life for each card exiled this way."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                for (final MagicCard card : new MagicCardList(CREATURE_CARD_FROM_GRAVEYARD.filter(it))) {
                    game.doAction(new ShiftCardAction(card, MagicLocationType.Graveyard, MagicLocationType.Exile));
                    if (card.isInExile()) {
                    game.doAction(new ChangeLifeAction(event.getPlayer(), 3));
                  }
               }
           });
        }
    }
]
