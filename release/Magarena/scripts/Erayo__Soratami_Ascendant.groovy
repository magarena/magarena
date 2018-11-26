[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (game.getSpellsCast() == 3) ?
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Flip SN."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new FlipAction(event.getPermanent()));
        }
    }
]
