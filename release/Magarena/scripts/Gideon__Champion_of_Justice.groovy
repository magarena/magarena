
def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        final int amt = source.getCounters(MagicCounterType.Charge);
        pt.set(amt,amt);
    }
};

def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        flags.add(MagicAbility.Indestructible);
    }
};
   
def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent,final Set<MagicSubType> flags) {
        flags.add(MagicSubType.Human);
        flags.add(MagicSubType.Soldier);
    }
    @Override
    public int getTypeFlags(final MagicPermanent permanent,final int flags) {
        return flags|MagicType.Creature.getMask();
    }
};

def PreventAllDamage = new MagicIfDamageWouldBeDealtTrigger(1) {
    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicDamage damage) {
        if (permanent == damage.getTarget()) {
            // Replacement effect. Generates no event or action.
            damage.setAmount(0);
        }
        return MagicEvent.NONE;
    }
};

[
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main),
        "+1") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                MagicPutCounterEvent.Self(
                    source,
                    MagicCounterType.Charge,
                    1
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a loyalty counter on SN for each creature target opponent controls."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final int amt = event.getPlayer().getOpponent().getNrOfPermanentsWithType(MagicType.Creature);
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.Charge,
                amt,
                true
            ));
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Main),
        "0") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until end of turn, Gideon, Champion of Justice becomes an indestructible Human Soldier creature " + 
                "with power and toughness each equal to the number of loyalty counters on him. " + 
                "He's still a planeswalker. Prevent all damage that would be dealt to him this turn."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicBecomesCreatureAction(event.getPermanent(),PT,AB,ST));
            game.doAction(new MagicAddTurnTriggerAction(event.getPermanent(), PreventAllDamage)); 
        }
    },
    new MagicPermanentActivation(
        [
            MagicCondition.SORCERY_CONDITION,
            MagicCondition.ABILITY_ONCE_CONDITION,
            MagicCondition.FIFTEEN_CHARGE_COUNTERS_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Main),
        "-15") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [
                new MagicRemoveCounterEvent(
                    source,
                    MagicCounterType.Charge,
                    15
                ),
                new MagicPlayAbilityEvent(source)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Exile all other permanents."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicTargetFilter<MagicPermanent> AllOtherPermanent = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_PERMANENT, 
                event.getPermanent()
            );
            final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(), AllOtherPermanent);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicRemoveFromPlayAction(
                    target,
                    MagicLocationType.Exile
                ));
            }        
        }
    }
]
