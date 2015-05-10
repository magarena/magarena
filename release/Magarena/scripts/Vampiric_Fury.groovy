[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Vampire creatures PN controls get +2/+0 " +
                "and gain first strike until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            VAMPIRE_CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it,2,0));
                game.doAction(new GainAbilityAction(it,MagicAbility.FirstStrike));
            }
        }
    }
]
