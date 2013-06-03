[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
            return (permanent == blocked) ?
                new MagicEvent(
                    permanent,
                    permanent.getOpponent(),
                    this,
                    "PN sacrifices a land."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent = event.getPlayer();
            if (opponent.controlsPermanent(MagicType.Land)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getPermanent(),
                    opponent,
                    MagicTargetChoice.SACRIFICE_LAND
                ));
            }
        }
    }
]
