[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all Zombie creature cards from your graveyard " +
                "to the battlefield tapped, then destroy all Humans."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicCard> zombies = ZOMBIE_CREATURE_CARD_FROM_GRAVEYARD.filter(event);
            for (final MagicCard target : zombies) {
                game.doAction(new ReanimateAction(
                    target,
                    player,
                    [MagicPlayMod.TAPPED]
                ));
            }
            game.doAction(new DestroyAction(HUMAN.filter(event)));
        }
    }
]
