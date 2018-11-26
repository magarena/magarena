def LEGENDARY_CREATURE_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target) {
        return target.hasType(MagicType.Legendary) && target.hasType(MagicType.Creature);
    }
}

def action = {
    final MagicGame game, final MagicEvent event ->
    final MagicCardList topCards = new MagicCardList(event.getRefCardList());
    event.processChosenCards(game, {
        game.doAction(new RevealAction(it));
        game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
        topCards.removeCard(it);
    });
    topCards.each {
        game.doAction(new ShiftCardAction(it, MagicLocationType.OwnersLibrary, MagicLocationType.BottomOfOwnersLibrary))
    }
}

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Flash),
        "AddToHand"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{X}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "PN looks at the top RN cards of PN's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int X = event.getRefInt();
            final MagicCardList topCards = player.getLibrary().getCardsFromTop(X);
            game.doAction(new LookAction(topCards, player, "top ${X} cards of your library"));
            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicFromCardListChoice(
                    topCards.findAll({ it.hasType(MagicType.Legendary) && it.hasType(MagicType.Creature) }),
                    1,
                    true
                ),
                topCards,
                action,
                "PN may reveal a legendary creature card from among them\$ and put it into PN's hand. " +
                "PN puts the rest on the bottom of PN's library in any order."
            ));
        }
    },
    new MagicStatic(MagicLayer.ModPT, LEGENDARY_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source, final MagicPermanent permanent, final MagicPowerToughness pt) {
            final int amount = LEGENDARY_CREATURE_YOU_CONTROL.except(permanent).filter(source.getController()).size();
            pt.add(amount,amount);
        }
    }
]

