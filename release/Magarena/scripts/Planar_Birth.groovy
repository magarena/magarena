[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all basic land cards from all graveyards to the battlefield tapped under their owners' control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> graveyard=
                game.filterCards(event.getPlayer(),BASIC_LAND_CARD_FROM_ALL_GRAVEYARDS);
            for (final MagicCard card : graveyard) {
                game.doAction(new MagicReanimateAction(card,card.getOwner(),[MagicPlayMod.TAPPED]));
            }
        }
    }
]
