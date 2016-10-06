def Pay2Life = MagicRegularCostEvent.build("{B}, Pay 2 life");
def Pay4Life = MagicRegularCostEvent.build("Pay 4 life");

[
    new MagicEquipActivation(Pay2Life, "Equip 2L", MagicTargetFilterFactory.CREATURE_YOU_CONTROL),
    new MagicEquipActivation(Pay4Life, "Equip 4L", MagicTargetFilterFactory.CREATURE_YOU_CONTROL)
]
