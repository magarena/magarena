def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo()) {
        game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
    }
}

[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) && cardOnStack.hasType(MagicType.Creature) ?
                new MagicEvent(
                    permanent,
                    cardOnStack.getController(),
                    this,
                    "SN deals 2 damage to PN unless PN pays {2}."
                ):
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}"))
                ),
                action,
                "PN may\$ pay {2}."
            ));
        }
    }
]
