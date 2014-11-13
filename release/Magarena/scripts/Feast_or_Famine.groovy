def choice = MagicTargetChoice.Negative("target nonartifact, nonblack creature");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicTargetChoice.NONE,
                    choice
                ),
                this,
                "Choose one\$ - put a 2/2 black Zombie creature token onto the battlefield; or destroy target nonartifact, nonblack creature. It can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(1)) {
                game.doAction(new MagicPlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("2/2 black Zombie creature token")
            ));
            } else if (event.isMode(2)) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicDestroyAction(it));
                });
            }
        }
    }
]
