[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Put a 1/1 black Wolf creature token with deathtouch onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("1/1 black Wolf creature token with deathtouch")));
        }
    },
    new MagicPlaneswalkerActivation(-1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
             return new MagicEvent(
                source,
                this,
                "Sacrifice a creature. If you do, search your library for a creature card, reveal it, put it into your hand, then shuffle your library."
            );
       }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent sac = new MagicSacrificePermanentEvent(event.getSource(),event.getPlayer(),MagicTargetChoice.SACRIFICE_CREATURE);
            if (sac.isSatisfied()) {
                final List<MagicCard> choiceList = event.getPlayer().filterCards(MagicTargetFilterFactory.CREATURE_CARD_FROM_LIBRARY);
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    new MagicFromCardListChoice(choiceList, 1, true),
                    MagicLocationType.OwnersHand
                ));           }    
       }
    },
    new MagicPlaneswalkerActivation(-3) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Creatures you control gain trample and get +X/+X until end of turn, where X is the number of creature cards in your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = game.filterCards(event.getPlayer(), MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD).size();  
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL
            );
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicGainAbilityAction(target, MagicAbility.Trample));
                game.doAction(new MagicChangeTurnPTAction(target, X, X));
            }      
        }
    }
]
