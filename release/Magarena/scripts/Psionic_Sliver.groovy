def PsionicDamage = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Damage"
) {

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
        return [new MagicTapEvent(source)];
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            NEG_TARGET_CREATURE_OR_PLAYER,
            new MagicDamageTargetPicker(2),
            this,
            "SN deals 2 damage to target creature or player\$ and 3 damage to itself."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicPermanent source=event.getPermanent();
        event.processTarget(game, {
            game.doAction(new DealDamageAction(source,it,2));
            game.doAction(new DealDamageAction(source,source,3));
        });
    }
};

[
    new MagicStatic(MagicLayer.Ability, SLIVER) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(PsionicDamage);
        }
    }
]
