[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isOpponent(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN sacrifices a creature."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer opponent=event.getPlayer();
            if (opponent.controlsPermanent(MagicType.Creature)) {
                game.addEvent(new MagicSacrificePermanentEvent(
                    event.getSource(),
                    opponent,
                    SACRIFICE_CREATURE
                ));
            }
        }
    }
]
