[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    player.getOpponent(),
                    this,
                    "PN puts the top ten cards of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicMillLibraryAction(event.getPlayer(),10));
        }
    }
]
