def action = {
    final MagicGame game, final MagicEvent event ->
    game.doAction(new ChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1));
    game.doAction(new MagicGainAbilityAction(
        event.getRefPermanent(),
        event.getChosenColor().getProtectionAbility()
    ));
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Put a +1/+1 counter on target creature\$ you control. "+
                "It gains protection from the color of your choice until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    event.getPlayer(),
                    MagicColorChoice.ALL_INSTANCE,
                    it,
                    action,
                    "Chosen color\$."
                ));
            });
        }
    }
]
