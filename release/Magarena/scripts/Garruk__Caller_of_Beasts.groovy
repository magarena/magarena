[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Reveal the top five cards of your library. Put all creature cards revealed this way into your hand and the rest on the bottom of your library in any order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList top5 = event.getPlayer().getLibrary().getCardsFromTop(5);
            for (final MagicCard top : top5) {
                game.doAction(new MagicRevealAction(top5));
                game.doAction(new MagicRemoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MoveCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasType(MagicType.Creature) ?
                      MagicLocationType.OwnersHand :
                      MagicLocationType.BottomOfOwnersLibrary
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
                "PN may put a green creature card from his or her hand onto the battlefield."
            );
       }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Put a green creature card onto the battlefield?",
                    A_GREEN_CREATURE_CARD_FROM_HAND
                )
            ));
        }
    },
    new MagicPlaneswalkerActivation(-7) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with \"Whenever you cast a creature spell, you may search your library for a creature card, put it onto the battlefield, then shuffle your library.\""
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            final MagicPlayer you = outerEvent.getPlayer();
            outerGame.doAction(new AddTriggerAction(
                new MagicWhenOtherSpellIsCastTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
                        return (cardOnStack.getController().getId() == you.getId() && cardOnStack.hasType(MagicType.Creature)) ?
                            new MagicEvent(
                                cardOnStack,
                                new MagicMayChoice(),
                                this,
                                "PN may\$ search his or her library for creature card and put that card onto the battlefield. Then shuffle PN's library."
                            ):
                            MagicEvent.NONE;
                    }
                    @Override
                    public void executeEvent(final MagicGame game, final MagicEvent event) {
                        if (event.isYes()) {
                            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                                event,
                                A_CREATURE_CARD_FROM_LIBRARY
                            ));
                        }
                    }
                }
            ));
        }
    }
]
