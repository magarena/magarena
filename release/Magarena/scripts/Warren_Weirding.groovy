def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new MagicSacrificeAction(it));
        if (it.hasSubType(MagicSubType.Goblin)){
            for (int i = 0; i < 2; i++) {
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 black Goblin Rogue creature token"),
                    MagicPlayMod.HASTE_UEOT
                ));
            }
        }
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER,
                this,
                "Target player\$ sacrifices a creature. " +
                "If a Goblin is sacrificed this way, that player puts two 1/1 black Goblin Rogue creature tokens onto the battlefield, " + 
                "and those tokens gain haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it,
                    SACRIFICE_CREATURE,
                    MagicSacrificeTargetPicker.create(),
                    action,
                    "Choose a creature to sacrifice\$."
                ));
            });
        }
    }
]
