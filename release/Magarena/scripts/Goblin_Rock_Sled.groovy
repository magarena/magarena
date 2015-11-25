[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent creature) {
            game.doAction(ChangeStateAction.Set(
                permanent,
                MagicPermanentState.DoesNotUntapDuringNext
            ));
            return MagicEvent.NONE;
        }
    }
]
