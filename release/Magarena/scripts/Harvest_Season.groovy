[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches PN's library for up to X basic land cards, where X is the number of tapped creatures PN controls, " +
                "and put those cards onto the battlefield tapped. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getPermanents().count({
                it.hasType(MagicType.Creature) && it.hasState(MagicPermanentState.Tapped)
            }).toInteger();
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    BASIC_LAND_CARD_FROM_LIBRARY,
                    amount,
                    true,
                    "up to ${amount} basic land cards from your library"
                ),
                MagicPlayMod.TAPPED
            ));
        }
    }
]

