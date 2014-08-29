[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.LoseEvasion),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source, "{1}{R}{R}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING,
                MagicLoseAbilityTargetPicker.create(MagicAbility.Flying),
                this,
                "SN deals 2 damage to target creature with flying\$. That creature loses flying until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicDamage damage = new MagicDamage(event.getSource(),it,2);
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicLoseAbilityAction(it,MagicAbility.Flying));
            });
        }
    }
]
