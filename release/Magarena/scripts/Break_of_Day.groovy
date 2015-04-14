[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Creatures PN controls get +1/+1 until end of turn. " +
                "If PN has 5 or less life, those creatures gain indestructible until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Boolean fatefulHour = MagicCondition.FATEFUL_HOUR.accept(event.getSource());
            game.filterPermanents(event.getPlayer(), CREATURE_YOU_CONTROL) each {
                game.doAction(new ChangeTurnPTAction(it, 1, 1));
                if (fatefulHour) {
                    game.doAction(new GainAbilityAction(it, MagicAbility.Indestructible));
                }
            }
        }
    }
]
