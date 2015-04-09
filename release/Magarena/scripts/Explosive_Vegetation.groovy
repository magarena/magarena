[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for up to two basic land cards and puts them onto the battlefield tapped. "+
                "Then PN shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    BASIC_LAND_CARD_FROM_LIBRARY,
                    2, 
                    true, 
                    "to put onto the battlefield tapped"
                ),
                MagicPlayMod.TAPPED
            ));
        }
    }
]
