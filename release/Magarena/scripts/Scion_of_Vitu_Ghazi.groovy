[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.hasState(MagicPermanentState.CastFromHand) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a 1/1 white Bird creature token with flying onto the battlefield, then populate."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 white and blue Bird creature token with flying")
            ));
            game.addEvent(new MagicPopulateEvent(event.getSource()));
        }
    }
]
