def public MagicPermanentActivation AddBloodCounter(final MagicPermanent source) {
    return new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent equipped) {
            return [
                new MagicTapEvent(equipped),
                new MagicSacrificePermanentEvent(equipped, SACRIFICE_CREATURE)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent equipped, final MagicPayedCost payedCost) {
            return new MagicEvent(
                equipped,
                source,
                this,
                "PN puts a blood counter on RN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getRefPermanent(), MagicCounterType.Blood, 1));
        }
    }
};

[
    
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(AddBloodCounter(source));
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) { 
            return MagicStatic.acceptLinked(game,source,target);
        }
    },
    
    new MagicPermanentActivation(
        [new MagicArtificialCondition(MagicConditionFactory.CounterAtLeast(MagicCounterType.Blood, 1))],
        new MagicActivationHints(MagicTiming.Removal),
        "Sacrifice"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificeEvent(source), new MagicPayManaCostEvent(source, "{B}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ sacrifices a permanent for each blood counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getPermanent().getCounters(MagicCounterType.Blood);
                game.logAppendMessage(event.getPlayer(), "("+amount+")");
                if (amount > 0) {
                    for (int i=amount;i>0;i++) {
                        game.addEvent(new MagicSacrificePermanentEvent(
                            event.getSource(),
                            it,
                            SACRIFICE_PERMANENT
                        ));
                    }
                }
            });
        }
    }
]
