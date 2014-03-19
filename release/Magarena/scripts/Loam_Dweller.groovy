[
    new MagicWhenYouCastSpiritOrArcaneTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return new MagicEvent(
                permanent,
                this,
                "PN may put a land card from his or her hand onto the battlefield tapped."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Put a basic land card onto the battlefield?",
                    MagicTargetChoice.BASIC_LAND_CARD_FROM_HAND,
                    MagicPlayMod.TAPPED
                )
            ));
        }
    }
]
