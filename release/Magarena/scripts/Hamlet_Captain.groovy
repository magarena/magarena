[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Other Human creatures PN controls get +1/+1 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_HUMAN_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                if (creature != event.getPermanent()) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                }
            }
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Other Human creatures PN controls get +1/+1 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_HUMAN_YOU_CONTROL);
            for (final MagicPermanent creature : targets) {
                if (creature != event.getPermanent()) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                }
            }
        }
    }
]
