[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            Set<MagicSubType> enchantedSubTypes = new HashSet<MagicSubType>();
            for (final MagicSubType subType: MagicSubType.values()) {
                if (permanent.hasSubType(subType)) {
                    enchantedSubTypes.add(subType);
                }
            }
            int amount = 0;
            CREATURE.except(permanent).filter(source.getController()) each {
                boolean hasSubType=false;
                for (final MagicSubType subType: enchantedSubTypes) {
                    if (it.hasSubType(subType)) {
                        hasSubType=true;
                    }
                }
                if (hasSubType) {
                    amount += 1;
                }
            }
            pt.add(amount*2,amount*2);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
