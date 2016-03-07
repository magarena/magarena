[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                permanent,
                player.getOpponent(),
                this,
                "PN gains control of SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new GainControlAction(event.getPlayer(),event.getPermanent()));
        }
    }
]
