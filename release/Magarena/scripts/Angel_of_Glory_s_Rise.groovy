[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all Zombies, then return all Human creature " +
                "cards from PN's graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final List<MagicPermanent> zombies =
                    game.filterPermanents(player,MagicTargetFilter.TARGET_ZOMBIE);
            for (final MagicPermanent target : zombies) {
                game.doAction(new MagicRemoveFromPlayAction(
                    target,
                    MagicLocationType.Exile
                ));
            }
            final List<MagicCard> humans =
                    game.filterCards(player,MagicTargetFilter.TARGET_HUMAN_CARD_FROM_GRAVEYARD);
            for (final MagicCard target : humans) {
                game.doAction(new MagicReanimateAction(
                    target,
                    player
                ));
            }
        }
    }
]
