def AB = new MagicTapManaActivation(MagicManaType.getList("{B} or {R}"));

[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(AB);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.SWAMP_CONDITION.accept(source);
        }
    }
]
