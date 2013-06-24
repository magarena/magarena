[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }
            final MagicPermanentList plist = permanent.getBlockingCreatures();
            boolean pump = false;
            for (final MagicPermanent blocker : plist) {
                if (blocker.hasColor(MagicColor.Blue) ||
                    blocker.hasColor(MagicColor.Black)) {
                    pump = true;
                }
            }
            return (pump) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +3/+3 until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(
                event.getPermanent(),
                3,
                3
            ));
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == blocker &&
                    blocked.isValid() &&
                    (blocked.hasColor(MagicColor.Blue) ||
                     blocked.hasColor(MagicColor.Black))) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +3/+3 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(
                event.getPermanent(),
                3,
                3
            ));
        }
    }
]

