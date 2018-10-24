[
    new OtherEntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            if (otherPermanent != permanent &&
                otherPermanent.isCreature() &&
                otherPermanent.isFriend(permanent)) {
                final int amount = permanent.getCounters(MagicCounterType.PlusOne);
                if (amount > 0 ) {
                    game.doAction(new ChangeCountersAction(permanent.getController(), otherPermanent, MagicCounterType.PlusOne, amount));
                    game.logAppendMessage(
                        permanent.getController(),
                        "${otherPermanent.getName()} enters the battlefield with an additional ${amount} +1/+1 counters on it."
                    );
                }
            }
            return MagicEvent.NONE;
        }
    }
]
