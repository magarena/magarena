
def AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
    @Override
    public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
        permanent.addAbility(MagicAbility.Indestructible, flags);
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

def PreventAllDamage = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicDamage damage) {
        if (permanent == damage.getTarget()) {
            // Replacement effect. Generates no event or action.
            damage.prevent();
        }
        return MagicEvent.NONE;
    }
};

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "Put a loyalty counter on SN for each creature target opponent\$ controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.getPlayer().getOpponent().getNrOfPermanents(MagicType.Creature);
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Loyalty,amt));
        }
    },
    new MagicPlaneswalkerActivation(0) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until end of turn, Gideon, Champion of Justice becomes a Human Soldier creature " +
                "with power and toughness each equal to the number of loyalty counters on him and gains indestructible. " +
                "He's still a planeswalker. Prevent all damage that would be dealt to him this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amt = event.getPermanent().getCounters(MagicCounterType.Loyalty);
            def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
                @Override
                public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
                    pt.set(amt,amt);
                }
            };

            game.doAction(new MagicBecomesCreatureAction(event.getPermanent(),PT,AB,ST));
            game.doAction(new MagicAddTurnTriggerAction(event.getPermanent(), PreventAllDamage));
        }
    },
    new MagicPlaneswalkerActivation(-15) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Exile all other permanents."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                PERMANENT.except(event.getPermanent())
            );
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicRemoveFromPlayAction(target, MagicLocationType.Exile));
            }
        }
    }
]
