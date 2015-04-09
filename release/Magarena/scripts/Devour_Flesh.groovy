def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new MagicSacrificeAction(it));
        game.doAction(new MagicChangeLifeAction(event.getPlayer(), it.getToughness()));
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_PLAYER_CONTROLS_CREATURE,
                this,
                "Target player\$ sacrifices a creature, then gains life equal to that creature's toughness."
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
