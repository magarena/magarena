[
    new MagicStatic(MagicLayer.ModPT, CREATURE) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent target, final MagicPowerToughness pt) {
            Set<MagicSubType> targetSubTypes = new HashSet<MagicSubType>();
            for (final MagicSubType subType: MagicSubType.values()) {
                if (target.hasSubType(subType)) {
                    targetSubTypes.add(subType);
                }
            }
            int amount = 0;
            CREATURE.except(target).filter(source.getController()) each {
                boolean hasSubType=false;
                for (final MagicSubType subType: targetSubTypes) {
                    if (it.hasSubType(subType)) {
                        hasSubType=true;
                    }
                }
                if (hasSubType) {
                    amount += 1;
                }
            }
            pt.add(amount,amount);
        }
    }
]
