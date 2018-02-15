[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Search your library and/or graveyard?"),
                this,
                "PN may\$ search PN's library and/or graveyard for a card named Huatli, Dinosaur Knight, reveal it, then put it into PN's hand. " +
                "If PN searched PN's library this way, shuffle it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    new MagicFromCardFilterChoice(
                        new MagicCardFilterImpl() {
                            @Override
                            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                                return target.getName().equalsIgnoreCase("Huatli, Dinosaur Knight");
                            }
                            @Override
                            public boolean acceptType(final MagicTargetType targetType) {
                                return targetType == MagicTargetType.Library || targetType == MagicTargetType.Graveyard;
                            }
                        },
                        1,
                        false,
                        "a card named Huatli, Dinosaur Knight from your library and/or graveyard"
                    ),
                    MagicLocationType.OwnersHand
                ));
            }
        }
    }
]

