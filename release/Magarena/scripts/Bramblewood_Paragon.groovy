[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            if (otherPermanent != permanent &&
                otherPermanent.isCreature() &&
                otherPermanent.isFriend(permanent) &&
                otherPermanent.hasSubType(MagicSubType.Warrior)) {
                game.doAction(new ChangeCountersAction(permanent.getController(),otherPermanent,MagicCounterType.PlusOne,1));
            }
            return MagicEvent.NONE;
        }
    }
]
