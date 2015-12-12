[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.hasState(MagicPermanentState.CastFromHand) == false ? 
                new MagicEvent(
                    permanent,
                    this,
                    "PN loses the game."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new LoseGameAction(event.getPlayer()));
        }
    }
]
