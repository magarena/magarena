[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may\$ search his or her library for up to two basic land cards, reveal them, and put them into PN's hand. "+
                "If PN does, PN shuffles his or her library."
            );
        }

       @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    new MagicFromCardFilterChoice(
                        MagicTargetFilterFactory.BASIC_LAND_CARD_FROM_LIBRARY,
                        2, 
                        true, 
                        "to put into your hand"
                    ),
                    MagicLocationType.OwnersHand
                ));
            }
        }
    }
]
