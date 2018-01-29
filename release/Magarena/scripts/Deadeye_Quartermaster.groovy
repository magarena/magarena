[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Search your deck?"),
                this,
                "PN may\$ search PN's library for an Equipment or Vehicle card, " +
                "reveal it, put it into PN's hand, and shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchToLocationEvent(
                event,
                new MagicFromCardFilterChoice(
                    card(MagicSubType.Equipment).or(MagicSubType.Vehicle).from(MagicTargetType.Library),
                    1,
                    false,
                    "an Equipment or Vehicle card from your library"
                ),
                MagicLocationType.OwnersHand
            ));
        }
    }
]

