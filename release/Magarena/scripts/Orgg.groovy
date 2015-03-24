def UNTAPPED_CREATURE_WITH_POWER_3_OR_GREATER = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isCreature() && target.isUntapped() && target.getPower() >= 3;
    } 
};

[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotAttack, flags);
        }
        @Override
        public boolean condition(final MagicGame game, final MagicPermanent source, final MagicPermanent target) {
            return game.getDefendingPlayer().controlsPermanent(UNTAPPED_CREATURE_WITH_POWER_3_OR_GREATER);
        }
    }
]
