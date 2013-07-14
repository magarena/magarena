[
    new MagicPlaneswalkerActivation(2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gains 1 life for each creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicChangeLifeAction(
                player,
                player.getNrOfPermanents(MagicType.Creature)
            ));
        }
    },
    new MagicPlaneswalkerActivation(-2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put three 1/1 white Soldier creature tokens onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(event.getPlayer(), TokenCardDefinitions.get("Soldier"), 3));
        }
    },
    new MagicPlaneswalkerActivation(-5) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Destroy all other permanents except for lands and tokens."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> AllOtherPermanent = new MagicOtherPermanentTargetFilter(
                MagicTargetFilter.TARGET_NONLAND_NONTOKEN_PERMANENT,
                event.getPermanent()
            );
            final Collection<MagicPermanent> targets = game.filterPermanents(event.getPlayer(), AllOtherPermanent);
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
