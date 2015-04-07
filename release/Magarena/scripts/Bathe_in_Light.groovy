def action = {
    final MagicGame game, final MagicEvent event ->
    game.filterPermanents(MagicTargetFilterFactory.CREATURE) each {
        if (it.getColorFlags() & event.getRefPermanent().getColorFlags()) {
            game.doAction(new MagicGainAbilityAction(it, event.getChosenColor().getProtectionAbility()));
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
