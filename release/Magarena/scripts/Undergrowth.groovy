def NONRED_CREATURES = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasColor(MagicColor.Red) && target.isCreature();
    } 
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Prevent all combat damage that would be dealt this turn. " +
                "If its additional cost was paid, SN doesn't affect combat damage that would be dealt by red creatures."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isKicked()) {
                NONRED_CREATURES.filter(game) each { 
                    game.doAction(new AddTurnTriggerAction(
                        it,
                        MagicPreventDamageTrigger.PreventCombatDamageDealtBy
                    ));
                }
            } else {
                game.doAction(new AddTurnTriggerAction(MagicPreventDamageTrigger.PreventCombatDamage));
            } 
        }
    }
]
