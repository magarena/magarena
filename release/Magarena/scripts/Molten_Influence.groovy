def choice = MagicTargetChoice.Negative("target instant or sorcery spell");

def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),4));
    } else {
        game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                this,
                "Counter target instant or sorcery spell\$ unless its controller has SN deal 4 damage to him or her."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice(),
                    it,
                    action,
                    "PN may\$ have SN deal 4 damage to him or her."
                ));
            });
        }
    }
]
