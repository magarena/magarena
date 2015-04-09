[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for three cards, exiles them, then shuffles his or her library." +
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final List<MagicCard> choiceList = event.getPlayer().filterCards(CARD_FROM_LIBRARY);
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                new MagicFromCardListChoice(choiceList, 3, true),
                MagicLocationType.Exile
            ));
            game.doAction(new MagicAddTriggerAction(
                MagicAtUpkeepTrigger.YouDraw(
                    event.getSource(), 
                    event.getPlayer()
                )
            ));
        }
    }
]
