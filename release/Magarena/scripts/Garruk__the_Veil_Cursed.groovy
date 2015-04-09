def AB1 = MagicRuleEventAction.create("Put a 1/1 black Wolf creature token with deathtouch onto the battlefield.");

[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return AB1.getEvent(source);
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
            final MagicEvent sac = new MagicSacrificePermanentEvent(event.getSource(),event.getPlayer(),SACRIFICE_CREATURE);
            if (sac.isSatisfied()) {
                game.addEvent(sac);
                game.addEvent(new MagicSearchToLocationEvent(
                    event,
                    A_CREATURE_CARD_FROM_LIBRARY,
                    MagicLocationType.OwnersHand
                ));
            }
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
            final int X = event.getPlayer().filterCards(CREATURE_CARD_FROM_GRAVEYARD).size();
            final Collection<MagicPermanent> targets = event.getPlayer().filterPermanents(CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicGainAbilityAction(target, MagicAbility.Trample));
                game.doAction(new MagicChangeTurnPTAction(target, X, X));
            }
        }
    }
]
