[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all creatures on the battlefield and all creature cards in graveyards to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> battlefield=
                game.filterPermanents(event.getPlayer(),CREATURE);
            final List<MagicCard> graveyard=
                game.filterCards(event.getPlayer(),CREATURE_CARD_FROM_ALL_GRAVEYARDS);
            for (final MagicPermanent target : battlefield) {
                game.doAction(new MagicRemoveFromPlayAction(target,MagicLocationType.OwnersHand));
            }
            for (final MagicCard card : graveyard) {
                game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
            }
        }
    }
]
