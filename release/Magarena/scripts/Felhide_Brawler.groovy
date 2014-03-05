def TARGET_MINOTAUR_YOU_CONTROL=new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) && target.hasSubType(MagicSubType.Minotaur);
        }
    }
[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            final MagicPermanentFilterImpl filter = new MagicOtherPermanentTargetFilter(TARGET_MINOTAUR_YOU_CONTROL, source);
            if (!source.getController().controlsPermanent(filter)) {
                permanent.addAbility(MagicAbility.CannotBlock, flags);
            }
        }
    }
]
