[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            for (final MagicPermanent target : permanent.getOpponent().getPermanents()) {
                if (target.hasSubType(MagicSubType.Human)) {
                    permanent.addAbility(MagicAbility.Haste, flags);
                    break;
                }
            }
        }
    }
]
