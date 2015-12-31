[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                cardOnStack.getController(),
                cardOnStack,
                this,
                "Counter RN unless PN pays {3}"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicCounterUnlessEvent(
                event.getSource(),
                event.getRefCardOnStack(),
                MagicManaCost.create("{3}")
            ));
        }
    }
]
