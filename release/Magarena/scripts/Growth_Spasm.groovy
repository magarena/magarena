[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a basic land card, and puts it onto the battlefield tapped, "+
                "then shuffle PN's library. PN puts a 0/1 colorless Eldrazi Spawn creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(event, MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY));
            game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("0/1 colorless Eldrazi Spawn creature token")
            ));
        }
    }
]
