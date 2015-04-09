[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getOpponent().getNrOfPermanents(MagicType.Land) >=
                   upkeepPlayer.getNrOfPermanents(MagicType.Land) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ search your library for up to three basic land cards, reveal them, and put them into your hand. "+
                    "If you do, shuffle your library."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
            final List<MagicCard> choiceList = event.getPlayer().filterCards(BASIC_LAND_CARD_FROM_LIBRARY);
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    new MagicFromCardListChoice(choiceList, 3, true),
                    MagicLocationType.OwnersHand
                ));         
            }
        }
    }
]
