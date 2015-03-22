def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.getRefPermanent().hasColor(MagicColor.Black)) {
        final Collection<MagicPermanent> creatures = game.filterPermanents(MagicTargetFilterFactory.BLACK_CREATURE);
        for (final MagicPermanent creature : creatures) {
            game.doAction(new MagicGainAbilityAction(
                creature,
                event.getChosenColor().getProtectionAbility()
            ));
        }
    }
    if (event.getRefPermanent().hasColor(MagicColor.Blue)) {
        final Collection<MagicPermanent> creatures = game.filterPermanents(MagicTargetFilterFactory.BLUE_CREATURE);
        for (final MagicPermanent creature : creatures) {
            game.doAction(new MagicGainAbilityAction(
                creature,
                event.getChosenColor().getProtectionAbility()
            ));
        }
    }
    if (event.getRefPermanent().hasColor(MagicColor.Green)) {
        final Collection<MagicPermanent> creatures = game.filterPermanents(MagicTargetFilterFactory.GREEN_CREATURE);
        for (final MagicPermanent creature : creatures) {
            game.doAction(new MagicGainAbilityAction(
                creature,
                event.getChosenColor().getProtectionAbility()
            ));
        }
    }
    if (event.getRefPermanent().hasColor(MagicColor.Red)) {
        final Collection<MagicPermanent> creatures = game.filterPermanents(MagicTargetFilterFactory.RED_CREATURE);
        for (final MagicPermanent creature : creatures) {
            game.doAction(new MagicGainAbilityAction(
                creature,
                event.getChosenColor().getProtectionAbility()
            ));
        }
    }
    if (event.getRefPermanent().hasColor(MagicColor.White)) {
        final Collection<MagicPermanent> creatures = game.filterPermanents(MagicTargetFilterFactory.WHITE_CREATURE);
        for (final MagicPermanent creature : creatures) {
            game.doAction(new MagicGainAbilityAction(
                creature,
                event.getChosenColor().getProtectionAbility()
            ));
        }
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                this,
                "Target creature\$ and each other creature that shares a color with it " +
                "gain protection from the color of your choice until end of turn."
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
