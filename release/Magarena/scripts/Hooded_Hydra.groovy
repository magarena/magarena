[
    new ThisTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            game.doAction(new ChangeCountersAction(permanent.getController(), permanent, MagicCounterType.PlusOne, 5));
            game.logAppendMessage(player,"${player.getName()} puts 5 +1/+1 counters on ${permanent.getName()}")
            return MagicEvent.NONE;
        }
    }
]
