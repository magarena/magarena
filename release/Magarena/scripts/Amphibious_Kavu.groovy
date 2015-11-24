[
    new SelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
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
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),3,3));
        }
    },
    new MagicWhenSelfBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (blocked.hasColor(MagicColor.Blue) ||
                     blocked.hasColor(MagicColor.Black)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +3/+3 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),3,3));
        }
    }
]
