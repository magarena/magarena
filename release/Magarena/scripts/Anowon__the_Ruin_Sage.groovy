[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Each player sacrifices a non-Vampire creature."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    player,
                    MagicTargetChoice.SACRIFICE_NON_VAMPIRE
                ));
            }
        }        
    }
]
