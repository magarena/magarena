def SLIVER_CREATURE_OR_PLAYER = new MagicTargetFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicTarget target) {
        return target.isPlayer() || (target.isCreature() && target.hasSubType(MagicSubType.Sliver));
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Permanent ||
               targetType==MagicTargetType.Player;
    }
};

def POS_TARGET_SLIVER_CREATURE_OR_PLAYER = new MagicTargetChoice(
    SLIVER_CREATURE_OR_PLAYER,
    MagicTargetHint.Positive,
    "target Sliver creature or player"
)

def CauteryDamage = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Removal),
    "Damage"
) {
    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{1}"),
            new MagicSacrificeEvent(source)
        ];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
            new MagicDamageTargetPicker(1),
            this,
            "SN deals 1 damage to target creature or player\$."
        );
    }
    @Override
     public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTarget(game,new MagicTargetAction() {
            public void doAction(final MagicTarget target) {
                final MagicDamage damage=new MagicDamage(event.getSource(),target,1);
                game.doAction(new MagicDealDamageAction(damage));
            }
        });
    }
};

def CauteryPrevent = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Prevent"
) {
    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{1}"),
            new MagicSacrificeEvent(source)
        ];
    }
    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            POS_TARGET_SLIVER_CREATURE_OR_PLAYER,
            MagicPreventTargetPicker.getInstance(),
            this,
            "Prevent the next 1 damage that would be dealt to target Sliver creature or player\$ this turn."
        );
    }
    @Override
     public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTarget(game,new MagicTargetAction() {
            public void doAction(final MagicTarget target) {
                game.doAction(new MagicPreventDamageAction(target,1));
            }
        });
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(CauteryDamage);
            permanent.addAbility(CauteryPrevent);
        }
    }
]
