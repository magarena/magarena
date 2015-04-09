[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for up to X basic land cards, where X is the number of lands PN controls, "+
                "and puts them onto the battlefield tapped. Then PN shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int lands = event.getPlayer().getNrOfPermanents(MagicType.Land);
            game.logAppendMessage(event.getPlayer()," (X="+lands+")");
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    BASIC_LAND_CARD_FROM_LIBRARY,
                    lands, 
                    true, 
                    "to put onto the battlefield tapped"
                ),
                MagicPlayMod.TAPPED
            ));
        }
    }
]
