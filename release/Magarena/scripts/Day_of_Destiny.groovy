def TARGET_LEGENDARY_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   target.isCreature() &&
                   target.hasType(MagicType.Legendary);
        }
    };
[
    new MagicStatic(
        MagicLayer.ModPT,
        TARGET_LEGENDARY_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(2,2);
        }
    }
]
