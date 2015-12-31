[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Until end of turn, creatures you control gain trample and get +X/+X, where X is the greatest power among creatures you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = CREATURE_YOU_CONTROL.filter(event);
            int power=0;
            for (final MagicPermanent creature : targets) {
                power=Math.max(power,creature.getPowerToughness().power());
            }
            for (final MagicPermanent creature : targets) {
                game.doAction(new ChangeTurnPTAction(creature,power,power));
                game.doAction(new GainAbilityAction(creature,MagicAbility.Trample));
            }
        }
    }
]
