[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            if (otherPermanent != permanent &&
                otherPermanent.isCreature() &&
                otherPermanent.isFriend(permanent) &&
                otherPermanent.hasSubType(MagicSubType.Wizard)) {
                game.doAction(new MagicChangeCountersAction(otherPermanent,MagicCounterType.PlusOne,1));
            }
            return MagicEvent.NONE;
        }
    }
]
