[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (game.getSpellsCast() == 3) ?
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Whenever the fourth spell of a turn is cast, flip SN."
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

