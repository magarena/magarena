[
    new MagicWhenSelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                this,
                "PN sacrifices a land."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer();
            if (opponent.controlsPermanent(MagicType.Land)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getPermanent(),
                    opponent,
                    SACRIFICE_LAND
                ));
            }
        }
    }
]
