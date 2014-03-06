[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            final MagicPermanentFilterImpl filter = new MagicTargetFilter.NameTargetFilter("Advocate of the Beast");
            if (!source.getController().controlsPermanent(filter)) {
                permanent.addAbility(MagicAbility.AttacksEachTurnIfAble, flags);
            }
        }
    }
]
