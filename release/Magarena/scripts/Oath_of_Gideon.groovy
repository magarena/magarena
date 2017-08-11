[
    new OtherEntersBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            if (otherPermanent.isPlaneswalker() && otherPermanent.isFriend(permanent)) {
                game.doAction(ChangeCountersAction.Enters(otherPermanent,MagicCounterType.Loyalty,1));
            }
            return MagicEvent.NONE;
        }
    }
]
