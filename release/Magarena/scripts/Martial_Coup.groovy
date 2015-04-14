[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Put "+x+" 1/1 white Soldier creature tokens onto the battlefield." +
                (x >= 5 ? " Destroy all other creatures.":"")
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player=event.getPlayer();
            int x = event.getCardOnStack().getX();
            if (x >= 5) {
                final Collection<MagicPermanent> targets = game.filterPermanents(player,CREATURE);
                for (final MagicPermanent target : targets) {
                    game.doAction(new DestroyAction(target));
                }
            }
            game.doAction(new MagicPlayTokensAction(
                player,
                TokenCardDefinitions.get("1/1 white Soldier creature token"),
                x
            ));
        }
    }
]
