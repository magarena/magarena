[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Attack),
        "-Block"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{2}{C}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each creature with toughness greater than its power can't block this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                if (it.getToughness() > it.getPower()) {
                    game.doAction(new GainAbilityAction(it, MagicAbility.CannotBlock));
                }
            }
        }
    }
]
