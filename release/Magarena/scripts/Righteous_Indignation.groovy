[
    new BlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked = blocker.getBlockedCreature();
            return (blocked.hasColor(MagicColor.Black) || blocked.hasColor(MagicColor.Red)) ?
                new MagicEvent(
                    permanent,
                    blocker,
                    this,
                    "RN gets +1/+1 until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getRefPermanent(), 1, 1));
        }
    }
]
