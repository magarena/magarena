def action = {
    final MagicGame game, final MagicEvent event ->
    CREATURE.filter(event) each {
        if (it == event.getRefPermanent() || it.shareColor(event.getRefPermanent())) {
            game.doAction(new GainAbilityAction(it, event.getChosenColor().getProtectionAbility()));
        }
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                this,
                "Target creature\$ and each other creature that shares a color with it " +
                "gain protection from the color of PN's choice until end of turn."
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
