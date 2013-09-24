
def QuilledDamage = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Removal),
    "Damage"
) {

     @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicTapEvent(source)
        ];
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.NEG_TARGET_ATTACKING_OR_BLOCKING_CREATURE,
            new MagicDamageTargetPicker(1),
            this,
            "SN deals 1 damage to target attacking or blocking creature\$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTarget(game,new MagicTargetAction() {
            public void doAction(final MagicTarget target) {
                final MagicDamage damage = new MagicDamage(event.getSource(),target,1);
                game.doAction(new MagicDealDamageAction(damage));
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
            permanent.addAbility(QuilledDamage);
        }
    }
]
