[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a Plains, Island, Swamp, or Mountain card and put that card onto the battlefield tapped. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                MagicTargetChoice.PLAINS_ISLAND_SWAMP_OR_MOUNTAIN_CARD_FROM_LIBRARY,
                MagicPlayMod.TAPPED
            ));
        }
    }
]
