[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isFriend(permanent) && otherPermanent != permanent?
                new MagicEvent(
                    permanent,
                    this,
                    "Each opponent puts the top card of his or her library into his or her graveyard." 
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MillLibraryAction(player.getOpponent(),1));
        }
    }
]
