def effect = MagicRuleEventAction.create("you may transform target Werewolf you control.")


[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Creatures PN control gain trample until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new GainAbilityAction(it, MagicAbility.Trample));
        }
                game.addEvent(effect.getEvent(event));
        }
    }
]
