def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isNo()) {
        game.addEvent(new MagicExileEvent(event.getRefPermanent()));
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_ATTACKING_CREATURE,
                amount,
                this,
                "Exile target attacking creature\$ unless its controller pays {X}. (X=${amount})"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = event.getRefInt();
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    it.getController(),
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{${amount}}"))
                    ),
                    it,
                    action,
                    "PN may\$ pay {${amount}}."
                ));
            });
        }
    }
]
