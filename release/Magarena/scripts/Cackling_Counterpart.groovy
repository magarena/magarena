[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                MagicCopyPermanentPicker.create(),
                this,
                "PN puts a token that's a copy of target creature\$ he controls onto the battlefield."
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
