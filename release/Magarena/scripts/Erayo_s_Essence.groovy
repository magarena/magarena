[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (permanent.isOpponent(cardOnStack.getController()) && permanent.getOpponent().getSpellsCast() == 0) ?
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Counter RN."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
        }
    }
]
