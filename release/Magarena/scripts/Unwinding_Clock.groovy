[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            if (permanent.isOpponent(upkeepPlayer)) {
                ARTIFACT_YOU_CONTROL.filter(permanent) each {
                    game.doAction(new UntapAction(it));
                }
            }
            return MagicEvent.NONE;
        }
    }
]
