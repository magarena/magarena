[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                    permanent,
                    enchanted.getController(),
                    this,
                    "SN deals 2 damage to PN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),2));
        }
    },

    new LeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return act.getPermanent() == permanent.getEnchantedPermanent() &&
               (act.to(MagicLocationType.Graveyard) || act.to(MagicLocationType.OpponentsGraveyard)) ?
                 new MagicEvent(
                    permanent,
                    this,
                    "PN creates a 2/2 red Gremlin creature token."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("2/2 red Gremlin creature token")
                    ));
        }
    }
]
