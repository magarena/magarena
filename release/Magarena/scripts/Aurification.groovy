def AB = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Defender, flags);
    }
    @Override
    public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
        return target.getCounters(MagicCounterType.Gold) > 0;
    }
};

def ST = new MagicStatic(MagicLayer.Type) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Wall);
    }
    @Override
    public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
        return target.getCounters(MagicCounterType.Gold) > 0;
    }
};

[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent.isController(damage.getTarget()) && damage.getSource().isCreature()) ?
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
                game.doAction(new MagicChangeCountersAction(it,MagicCounterType.Gold,1));
                game.doAction(new AddStaticAction(it, AB));
                game.doAction(new AddStaticAction(it, ST));
            });
        }
    },
    new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicRemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "Remove all gold counters from all creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.filterPermanents(CREATURE) each {
                game.doAction(new MagicChangeCountersAction(it, MagicCounterType.Gold, -it.getCounters(MagicCounterType.Gold)));
            }
        }
    }
]
