[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            game.doAction(MagicChangeStateAction.Set(
                event.getPermanent(),
                MagicPermanentState.DoesNotUntapDuringNext
            ));
            return MagicEvent.NONE;
        }
    }
]
