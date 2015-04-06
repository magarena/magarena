[
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            return permanent == blocker.getBlockedCreature() && blocker.isArtifact() && blocker.isCreature() ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +3/+3 until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(), 3, 3));
        }
    }
]
