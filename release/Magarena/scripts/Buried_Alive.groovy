[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for up to three creature cards and puts them into his or her graveyard. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> choiceList = event.getPlayer().filterCards(CREATURE_CARD_FROM_LIBRARY);
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                new MagicFromCardListChoice(choiceList, 3, true),
                MagicLocationType.Graveyard
            ));
        }
    }
]
