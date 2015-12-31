[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$. " +
                "Its controller may search his or her library for a basic land card, put that card onto the battlefield tapped, then shuffle his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.Exile));
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice(
                        "Search for a basic land card?",
                        A_BASIC_LAND_CARD_FROM_LIBRARY
                    ),
                    MagicPlayMod.TAPPED
                ));
            });
        }
    }
]
