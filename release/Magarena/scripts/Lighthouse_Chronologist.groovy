[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int level = permanent.getCounters(MagicCounterType.Level);
            if (level >= 7) {
                pt.set(3,5);
            } else if (level >= 4) {
                pt.set(2,4);
            }
        }
    },
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return (permanent.getCounters(MagicCounterType.Level) >= 7 &&
                    permanent.isOpponent(eotPlayer)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN takes an extra turn after this one."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeExtraTurnsAction(event.getPlayer(),1));
        }
    }
]
