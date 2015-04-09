[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicCopyPermanentPicker.create(),
                this,
                "PN puts a token that's a copy of target creature\$ onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                game.doAction(new MagicPlayTokenAction(
                    player,
                    it
                ));
            });
        }
    }
]
