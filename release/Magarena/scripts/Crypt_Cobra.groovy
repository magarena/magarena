[
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            final MagicPlayer opponent = player.getOpponent();
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    opponent,
                    this,
                    "PN gets a poison counter."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangePoisonAction(event.getPlayer(),1));
        }
    }
]
