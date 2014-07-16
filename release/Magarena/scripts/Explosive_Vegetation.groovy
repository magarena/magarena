[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for up to two basic land cards and put them onto the battlefield tapped. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY,
                MagicPlayMod.TAPPED
            ));
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY,
                MagicPlayMod.TAPPED
            ));
        }
    }
]