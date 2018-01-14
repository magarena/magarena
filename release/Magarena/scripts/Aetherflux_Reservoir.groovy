[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return permanent.isController(cardOnStack.getController()) ?
                new MagicGainLifeEvent(
                    permanent,
                    permanent.getController(),
                    1 + permanent.getController().getSpellsCast()
                )
                :
                MagicEvent.NONE;
        }
    }
]

