[
    new MagicStatic(
        MagicLayer.Ability,
        CREATURE_YOU_CONTROL
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Trample, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.getCounters(MagicCounterType.PlusOne) > 0;
        }
    },
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            if (otherPermanent != permanent &&
                otherPermanent.isCreature() &&
                otherPermanent.isFriend(permanent) &&
                otherPermanent.hasSubType(MagicSubType.Warrior)) {
                game.doAction(new MagicChangeCountersAction(otherPermanent,MagicCounterType.PlusOne,1));
            }
            return MagicEvent.NONE;
        }
    }
]
