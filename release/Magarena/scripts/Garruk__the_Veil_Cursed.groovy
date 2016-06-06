[
    new MagicPlaneswalkerActivation(-1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Sacrifice a creature. If PN does, PN searches his or her library for a creature card, reveals it, puts it into his or her hand, then shuffles his or her library."
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
                "Creatures PN controls gain trample and get +X/+X until end of turn, where X is the number of creature cards in his or her graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int X = CREATURE_CARD_FROM_GRAVEYARD.filter(event).size();
            CREATURE_YOU_CONTROL.filter(event.getPlayer()) each {
                game.doAction(new GainAbilityAction(it, MagicAbility.Trample));
                game.doAction(new ChangeTurnPTAction(it, X, X));
            }
        }
    }
]
