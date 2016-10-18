def TRASNFORM = MagicRuleEventAction.create("Transform all Humans.");

def NONWEREWOLF_CREATURES = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasSubType(MagicSubType.Werewolf) && 
               !target.hasSubType(MagicSubType.Wolf) && 
               target.isCreature();
    }
};

[    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
               "Transform all Humans. " + 
               "Prevent all combat damage that would be dealt this turn by creatures other than Werewolves and Wolves."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            TRASNFORM.getAction().executeEvent(game, event);
            game.doAction(new AddTurnTriggerAction(
                PreventDamageTrigger.PreventCombatDamageDealtBy(NONWEREWOLF_CREATURES)
            ));
        }
    }
]
