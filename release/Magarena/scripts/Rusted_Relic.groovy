[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (MagicCondition.METALCRAFT_CONDITION.accept(permanent)) {
                pt.set(5,5);
            }
        }
    }, 
	new MagicStatic(MagicLayer.Type) {
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			if (MagicCondition.METALCRAFT_CONDITION.accept(permanent)) {
				return flags|MagicType.Creature.getMask();
			}else{
				return flags;
			}
		}
    },
	new MagicStatic(MagicLayer.Type) {
        @Override
        public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
			if (MagicCondition.METALCRAFT_CONDITION.accept(permanent)) {
				flags.add(MagicSubType.Golem);
			}
        }
    },
]
