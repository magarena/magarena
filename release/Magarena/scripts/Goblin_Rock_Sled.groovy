[
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            game.doAction(MagicChangeStateAction.Set(
                permanent,
                MagicPermanentState.DoesNotUntapDuringNext
            ));
            return MagicEvent.NONE;
        }
    }
]
