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
            for (int i=2;i>0;i--) {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    new MagicMayChoice(
                        "Search for a basic land card?",
                        MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
                    ),
                    MagicPlayMod.TAPPED
                ));
            }
        }
    }
]
// Both lands should enter the battlefield at the same time. Should be single choice of multiple 'targets'
