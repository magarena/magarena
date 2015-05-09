[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 1/1 green Elf Warrior creature token onto the battlefield " +
                "for each Elf creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("1/1 green Elf Warrior creature token"),
                player.getNrOfPermanents(ELF)
            ));
        }
    }
]
