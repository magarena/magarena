[
    new MagicWhenOtherComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            if (otherPermanent != permanent &&
                otherPermanent.isCreature() &&
                otherPermanent.isFriend(permanent) &&
                otherPermanent.hasSubType(MagicSubType.Wizard)) {
                game.doAction(MagicChangeCountersAction.Enters(otherPermanent,MagicCounterType.PlusOne,1));
            }
            return MagicEvent.NONE;
        }
    }
]
