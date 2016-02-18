[
    new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            int amount = 0;
            final Set<MagicSubType> self = permanent.getSubTypes();
            self.retainAll(MagicSubType.ALL_CREATURES);
            CREATURE_YOU_CONTROL.except(permanent).filter(source) each {
                for (final MagicSubType subType : self) {
                    if (it.hasSubType(subType)) {
                        amount += 1;
                        break;
                    }
                }
            }
            pt.add(amount,amount);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicStatic.acceptLinked(game, source, target);
        }
    }
]
