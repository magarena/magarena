[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            new MagicEvent(
                permanent,
                cardOnStack,
                this,
                "Sacrifice SN and counter RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
            game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
        }
    }
]
