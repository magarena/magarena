[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicSacrificeTargetPicker.create(),
                this,
                "Target creature's\$ controller sacrifices it, then creates X 1/1 green and white Elf Warrior creature tokens, where X is that that creature's power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = it.getPower();
                game.logAppendX(event.getPlayer(),amount);
                final MagicPlayer controller=it.getController();
                game.doAction(new SacrificeAction(it));
                game.doAction(new PlayTokensAction(
                    controller,
                    CardDefinitions.getToken("1/1 green and white Elf Warrior creature token"),
                    amount
                ));
            });
        }
    }
]
