[
    new MagicStatic(MagicLayer.Ability, CREATURE) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.Defender, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.hasCounters(MagicCounterType.Gold);
        }
    },

    new MagicStatic(MagicLayer.Type, CREATURE) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
            flags.add(MagicSubType.Wall);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return target.hasCounters(MagicCounterType.Gold);
        }
    },

    new ThisLeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "Remove all gold counters from all creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                game.doAction(new ChangeCountersAction(it, MagicCounterType.Gold, -it.getCounters(MagicCounterType.Gold)));
            }
        }
    }
]
