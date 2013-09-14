def AB = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(
            final MagicPermanent source,
            final MagicPermanent permanent,
            final Set<MagicAbility> flags) {
        flags.add(MagicAbility.Defender);
    }
    @Override
    public boolean condition(
            final MagicGame game,
            final MagicPermanent source,
            final MagicPermanent target) {
        return target.getCounters(MagicCounterType.Gold) > 0;
    }
};

def ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(
            final MagicPermanent permanent,
            final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Wall);
    }
    @Override
    public boolean condition(
            final MagicGame game,
            final MagicPermanent source,
            final MagicPermanent target) {
        return target.getCounters(MagicCounterType.Gold) > 0;
    }
};

[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent.isController(damage.getTarget()) &&
                    damage.getSource().isCreature()) ?
                new MagicEvent(
                    permanent,
                    damage.getSource(),
                    this,
                    "PN puts a gold counter on RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processRefPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.Gold,1,true));
                game.doAction(new MagicAddStaticAction(creature, AB));
                game.doAction(new MagicAddStaticAction(creature, ST));
            } as MagicPermanentAction);
        }
    },
    new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicRemoveFromPlayAction act) {
            return act.isPermanent(permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Remove all gold counters from all creatures."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent permanent : targets) {
                final int amount = permanent.getCounters(MagicCounterType.Gold);
                if (amount > 0) {
                    game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.Gold,
                        -amount,
                        true
                    ));
                }
            }
        }
    }
]
