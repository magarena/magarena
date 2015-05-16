[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicPowerTargetPicker.create(),
                this,
                "PN gains life equal to target creature's\$ power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer()
                int amount = it.getPower();
                game.logAppendMessage(player,"("+amount+")");
                game.doAction(new ChangeLifeAction(player, amount));
            });
        }
    }
]
