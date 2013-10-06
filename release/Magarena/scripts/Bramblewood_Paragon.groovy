[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL
    ) {
        @Override
		public void modAbilityFlags(
				final MagicPermanent source,
				final MagicPermanent permanent,
				final Set<MagicAbility> flags) {
			flags.add(MagicAbility.Trample);
		}
		@Override
		public boolean condition(
				final MagicGame game,
				final MagicPermanent source,
				final MagicPermanent target) {
			return target.getCounters(MagicCounterType.PlusOne) > 0;
		}
    },
	new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
                    otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Warrior)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "Put a +1/+1 counter on RN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1,true));
        }
    },
]
