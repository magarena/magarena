def Equip0 = new MagicEquipActivation(MagicManaCost.create("{0}"), "Equip {0}");

def EQUIPMENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isEquipment() && target.isController(player);
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        EQUIPMENT_YOU_CONTROL
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(Equip0);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return MagicCondition.METALCRAFT_CONDITION.accept(source);
        }
    }
]
