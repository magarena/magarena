[
    new SelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            final MagicPermanentList plist = permanent.getBlockingCreatures();
            boolean pump = false;
            for (final MagicPermanent blocker : plist) {
                if (blocker.hasSubType(MagicSubType.Orc)) {
                    pump = true;
                }
            }
            return (pump) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +0/+2 until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(
                event.getPermanent(),
                0,
                2
            ));
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == blocker &&
                    blocked.isValid() &&
                    (blocked.hasSubType(MagicSubType.Orc))) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +0/+2 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(
                event.getPermanent(),
                0,
                2
            ));
        }
    }
]

