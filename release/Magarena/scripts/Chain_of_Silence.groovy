def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicEvent sac = new MagicSacrificePermanentEvent(event.getSource(),event.getPlayer(),SACRIFICE_LAND); 
    if (event.isYes() && sac.isSatisfied()) {
        game.addEvent(sac);
        game.doAction(new CopyCardOnStackAction(event.getPlayer(),event.getCardOnStack()));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                this,
                "Prevent all damage target creature\$ would deal this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddTurnTriggerAction(it,PreventDamageTrigger.PreventDamageDealtBy));
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice("Sacrifice a land?"),
                    action,
                    "PN may sacrifice a land\$. If PN does, he or she may copy this spell and may choose a new target for that copy."
                ));
            });
        }
    }
]
