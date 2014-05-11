[
    new MagicWhenBecomesUntappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player = otherPermanent.getController();
            return new MagicEvent(
                permanent,
                otherPermanent,
                this,
                player.getName()+" puts the top card of his or her library into his or her graveyard."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getRefPermanent().getController();
            game.doAction(new MagicMillLibraryAction(player,1));
        }
    }
]
