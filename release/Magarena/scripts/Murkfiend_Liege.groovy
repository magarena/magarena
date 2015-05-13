[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            if (permanent.isOpponent(upkeepPlayer)) {
                CREATURE_YOU_CONTROL.filter(permanent) each {
                    if (it.isTapped() && (it.hasColor(MagicColor.Blue) || it.hasColor(MagicColor.Green))) {
                        game.doAction(new UntapAction(it));
                    }
                }
            }
            return MagicEvent.NONE;
        }
    }
]
