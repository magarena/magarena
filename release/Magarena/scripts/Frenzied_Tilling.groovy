[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target land\$. " +
                "PN searches his or her library for a basic land card and put that card onto the battlefield tapped. Then shuffle PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    A_BASIC_LAND_CARD_FROM_LIBRARY,
                    MagicPlayMod.TAPPED
                ));
            });
        }
    }
]
