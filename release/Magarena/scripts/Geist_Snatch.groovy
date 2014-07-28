
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_SPELL,
                this,
                "Counter target creature spell\$. " +
                "Put a 1/1 blue Spirit creature token with flying onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                game.doAction(new MagicCounterItemOnStackAction(it));
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 blue Spirit creature token with flying")
                ));
            });
        }
    }
]
