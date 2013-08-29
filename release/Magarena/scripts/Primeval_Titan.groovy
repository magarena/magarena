    [
        new MagicWhenComesIntoPlayTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {     
                return new MagicEvent(
                permanent,
                this,
                "PN may search his or her library for up to two basic land cards and put them onto the battlefield tapped, then shuffle his or her library."
             );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    new MagicMayChoice(
                        "Search for a basic land card?",
                        MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
                    ),
                    MagicPlayMod.TAPPED
                ));
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    new MagicMayChoice(
                        "Search for a basic land card?",
                        MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
                    ),
                    MagicPlayMod.TAPPED
                ));
            }
        },
        new MagicWhenAttacksTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
                return (permanent == creature) ?
                new MagicEvent(
                   permanent,
                   this,
                   "PN may search his or her library for up to two basic land cards and put them onto the battlefield tapped, then shuffle his or her library."
                ):
             MagicEvent.NONE;      
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    new MagicMayChoice(
                        "Search for a basic land card?",
                        MagicTargetChoice.BASIC_LAND_CARD_FROM_LIBRARY
                    ),
                    MagicPlayMod.TAPPED
                ));
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
    ]