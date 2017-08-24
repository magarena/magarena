[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isOpponent(upkeepPlayer) &&
                    permanent.getCounters(MagicCounterType.Quest) >= 4) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap all creatures you control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new UntapAction(it));
            }
        }
    }
]
