[
    new MagicWhenBecomesUntappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return new MagicEvent(
                permanent,
                otherPermanent.getController(),
                this,
                "PN puts the top card of his or her library into his or her graveyard."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MillLibraryAction(event.getPlayer(),1));
        }
    }
]
