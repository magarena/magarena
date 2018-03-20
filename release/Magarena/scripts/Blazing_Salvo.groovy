def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),5));
    } else {
        game.doAction(new DealDamageAction(event.getSource(),event.getRefPermanent(),3));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 3 damage to target creature\$ unless that creature's controller has SN deal 5 damage to him or her."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice("have SN deal 5 damage to you?"),
                    it,
                    action,
                    "PN may\$ have SN deal 5 damage to him or her. If PN doesn't, SN deals 3 damage to RN."
                ));
            });
        }
    }
]
