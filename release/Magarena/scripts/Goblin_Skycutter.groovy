[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.LoseEvasion),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificeEvent(source)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE_WITH_FLYING,
                new MagicLoseAbilityTargetPicker(MagicAbility.Flying),
                this,
                "SN deals 2 damage to target creature with flying\$. That creature loses flying until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,2));
                game.doAction(new MagicLoseAbilityAction(it,MagicAbility.Flying));
            });
        }
    }
]
