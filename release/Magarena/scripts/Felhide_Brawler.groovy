def MINOTAUR_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) && target.hasSubType(MagicSubType.Minotaur);
    }
};

[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MagicAbility.CannotBlock, flags);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            final MagicPermanentFilterImpl filter = new MagicOtherPermanentTargetFilter(MINOTAUR_YOU_CONTROL, source);
            return source.getController().controlsPermanent(filter) == false;
        }
    }
]
