[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked=permanent.getBlockedCreature();
            return (permanent==blocker &&
                    blocked.isValid() &&
                    blocked.hasColor(MagicColor.Black)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +1/+2 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),1,2));
        }
    }
]
