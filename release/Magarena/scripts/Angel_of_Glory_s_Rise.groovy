[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all Zombies, then return all Human creature cards from PN's graveyard to the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            ZOMBIE.filter(event) each {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.Exile));
            }
            HUMAN_CREATURE_CARD_FROM_GRAVEYARD.filter(event) each {
                game.doAction(new ReanimateAction(it, player));
            }
        }
    }
]
