def ZeroEquip = new MagicEquipActivation(MagicManaCost.create("{0}"));
def FreeAttach = new MagicPermanentActivation(
    [MagicCondition.SORCERY_CONDITION, MagicCondition.NOT_CREATURE_CONDITION],
    new MagicActivationHints(MagicTiming.Equipment),
    "FreeEquip"
) {

    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return ZeroEquip.getCostEvent(source);
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return ZeroEquip.getPermanentEvent(source, payedCost);
    } 
};
def EQUIPMENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return (target.isEquipment() && target.isController(player));
        }
    };
[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isEquipment() && (otherPermanent.getController() == permanent.getController())) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        1,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    "PN may\$ draw a card."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicDrawAction(event.getPlayer()));
            }
        }
    },
    new MagicStatic(
        MagicLayer.Ability,
        EQUIPMENT_YOU_CONTROL
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (MagicCondition.METALCRAFT_CONDITION.accept(permanent)) {
                permanent.addAbility(FreeAttach);
            }
        }
    }
]
