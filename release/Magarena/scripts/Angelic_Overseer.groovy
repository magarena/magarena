[
    new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            for (final MagicPermanent target : permanent.getController().getPermanents()) {
                if (target.hasSubType(MagicSubType.Human)) {
                    flags.add(MagicAbility.Hexproof);
                    flags.add(MagicAbility.Indestructible);
                    break;
                }
            }
        }
    }
]
