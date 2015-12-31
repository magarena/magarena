def ATTACKING_CREATURE_WITHOUT_FLYING = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return target.isCreature() &&
            target.isAttacking() &&
            target.hasAbility(MagicAbility.Flying) == false
            target.isController(player.getOpponent());
    }
};

def NEG_TARGET_ATTACKING_CREATURE_WITHOUT_FLYING = new MagicTargetChoice(
    ATTACKING_CREATURE_WITHOUT_FLYING,
    MagicTargetHint.Negative,
    "target creature without flying that's attacking you"
);


[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{3}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_ATTACKING_CREATURE_WITHOUT_FLYING,
                MagicExileTargetPicker.create(),
                this,
                "SN deals 1 damage to target creature without flying that's attacking PN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getPermanent(), it, 1));
            });
        }
    }
]
