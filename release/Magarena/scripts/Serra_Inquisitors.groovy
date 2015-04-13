[
    new MagicWhenSelfBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            final MagicPermanentList plist = permanent.getBlockingCreatures();
            boolean pump = false;
            for (final MagicPermanent blocker : plist) {
                if (blocker.hasColor(MagicColor.Black)) {
                    pump = true;
                }
            }
            return (pump) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +2/+0 until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(
                event.getPermanent(),
                2,
                0
            ));
        }
    }
]

