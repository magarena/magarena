def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        game.doAction(new SacrificeAction(it));
        final int toughness = it.getToughness();
        game.doAction(new ChangeLifeAction(event.getRefPlayer(),toughness));
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ sacrifices a creature. " +
                "PN gains life equal to that creature's toughness."
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
                    event.getPlayer(),
                    action,
                    "Choose a creature to sacrifice\$."
                ));
            });
        }
    }
]
