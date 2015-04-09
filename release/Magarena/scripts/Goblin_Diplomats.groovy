[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.MustAttack),
        "Attacks"
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
                this,
                "Each creature attacks this turn if able."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicGainAbilityAction(target,MagicAbility.AttacksEachTurnIfAble));
            }
        }
    }
]
