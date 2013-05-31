def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        flags.add(MagicAbility.Wither);
    }
};
[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }
            final MagicPermanentList plist = new MagicPermanentList(permanent.getBlockingCreatures());
            return new MagicEvent(
                permanent,
                plist,
                this,
                "Blocking creatures gain wither until end of turn."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentList plist = event.getRefPermanentList(); 
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicAddStaticAction(blocker,AB));
            }
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent attacker = permanent.getBlockedCreature();
            return (permanent == blocker && attacker.isValid()) ?
                new MagicEvent(
                    permanent,
                    attacker,
                    this,
                    "RM gains wither until end of turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicAddStaticAction(event.getRefPermanent(),AB));
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            return (permanent == damage.getSource() &&
                    permanent.isOpponent(damage.getTarget()) &&
                    permanent.getCounters(MagicCounterType.MinusOne) > 0) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Remove all -1/-1 counters from SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final int amount = permanent.getCounters(MagicCounterType.MinusOne);
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.MinusOne,
                -amount,
                true
            ));
        }
    }
]
