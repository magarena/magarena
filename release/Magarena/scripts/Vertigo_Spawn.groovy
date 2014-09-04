[
    new MagicWhenSelfBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent blocked=permanent.getBlockedCreature();
            return blocked.isCreature() ?
                new MagicEvent(
                    permanent,
                    blocked,
                    this,
                    "Tap RN. It doesn't untap during its controller's next untap step."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicTapAction(event.getRefPermanent()));
            game.doAction(MagicChangeStateAction.Set(
                event.getRefPermanent(),
                MagicPermanentState.DoesNotUntapDuringNext
            ));
        }
    }
]
