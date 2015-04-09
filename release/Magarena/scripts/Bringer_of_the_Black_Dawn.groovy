[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ pay 2 life to search his or her library for a card, then shuffle his or her library and put that card on top of it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                game.addEvent(new MagicPayLifeEvent(event.getPermanent(), 2));
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    A_CARD_FROM_LIBRARY,
                    MagicLocationType.TopOfOwnersLibrary
                ));
            }
        }
    }
]
