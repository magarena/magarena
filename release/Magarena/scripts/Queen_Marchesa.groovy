[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getOpponent().hasState(MagicPlayerState.Monarch) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN creates a 1/1 black Assassin creature token with deathtouch and haste."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(event.getPlayer(),CardDefinitions.getToken("1/1 black Assassin creature token with deathtouch and haste"),1));
        }
    }
]
