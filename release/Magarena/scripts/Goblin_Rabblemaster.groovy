def GOBLIN_CREATURE_YOU_CONTROL=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Goblin) && target.isCreature() && target.isController(player);
    } 
};
[
    new MagicStatic(
        MagicLayer.Ability,
        GOBLIN_CREATURE_YOU_CONTROL
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.AttacksEachTurnIfAble, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    },
    new MagicAtBeginOfCombatTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer attackingPlayer) {
            return permanent.getController() == attackingPlayer ? 
                new MagicEvent(
                    permanent,
                    this,
                    "Put a 1/1 red Goblin creature token with haste onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("1/1 red Goblin creature token with haste")));
        }
    },
    new MagicWhenSelfAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                    permanent,
                    this,
                    "SN gets +1/+0 until end of turn for each other attacking Goblin."
                );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent creature=event.getPermanent();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(creature.getController(),MagicTargetFilterFactory.ATTACKING_GOBLIN);
            //excluding itself
            final int power = (targets.size() - 1);
            game.doAction(new MagicChangeTurnPTAction(creature,power,0));
        }
    }
]
