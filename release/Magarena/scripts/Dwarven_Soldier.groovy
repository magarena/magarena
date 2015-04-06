[
    new MagicWhenBlocksOrBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            boolean pump = false;
            final MagicPermanent target = permanent == blocker ? blocker : blocker.getBlockedCreature() ;
            final MagicPermanentList plist = target.getBlockingCreatures();
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
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),0,2));
        }
    }
]

