[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int x = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals " + x + " damage to each creature without flying." +
                (x >= 4 ? " Destroy all artifacts." : "") +
                (x >= 8 ? " PN creates an 8/8 red Lizard creature token." : "")
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicSource source = event.getSource();
            int x = event.getCardOnStack().getX();
            if (x >= 8) {
                game.doAction(new PlayTokensAction(player, CardDefinitions.getToken("8/8 red Lizard creature token"), 1));
            }
            if (x >= 4) {
                game.doAction(new DestroyAction(ARTIFACT.filter(event)));
            }
            CREATURE_WITHOUT_FLYING.filter(event) each {
                game.doAction(new DealDamageAction(source, it, x));
            }
        }
    }
]
