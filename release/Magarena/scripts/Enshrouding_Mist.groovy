def preventDamageTrigger = new IfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        if (damage.getTarget() == permanent) {
            // Replacement effect. Generates no event or action.
            damage.prevent();
        }
        return MagicEvent.NONE;
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +1/+1 until end of turn. "+
                "Prevent all damage that would be dealt to it this turn. "+
                "If it's renowned, untap it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,1,1));
                game.doAction(new AddTurnTriggerAction(it, preventDamageTrigger));
                if (it.hasState(MagicPermanentState.Renowned)) {
                    game.doAction(new UntapAction(it));
                }
            });
        }
    }
]

