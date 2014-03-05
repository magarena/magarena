def action = {
    final MagicGame game, final MagicEvent event ->
    event.processTargetPermanent(game, {
        final MagicPermanent permanent ->
        game.doAction(new MagicSacrificeAction(permanent));
        game.doAction(new MagicChangeLifeAction(event.getPlayer(), permanent.getToughness()));
    });
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player\$ sacrifices a creature, then gains life equal to that creature's toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicPlayer player ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    player,
                    MagicTargetChoice.SACRIFICE_CREATURE,
                    MagicSacrificeTargetPicker.create(),
                    action,
                    "Choose a creature to sacrifice\$."
                ));
            });
        }
    }
]
