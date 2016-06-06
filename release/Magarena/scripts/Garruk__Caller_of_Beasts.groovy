[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN reveals the top five cards of his or her library. PN puts all creature cards revealed this way into his or her hand and the rest on the bottom of his or her library in any order."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList top5 = event.getPlayer().getLibrary().getCardsFromTop(5);
            game.doAction(new RevealAction(top5));
            for (final MagicCard top : top5) {
                game.doAction(new ShiftCardAction(
                    top,
                    MagicLocationType.OwnersLibrary,
                    top.hasType(MagicType.Creature) ?
                        MagicLocationType.OwnersHand :
                        MagicLocationType.BottomOfOwnersLibrary
                ));
            }
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
            final long pId = outerEvent.getPlayer().getId();
            outerGame.doAction(new AddTriggerAction(
                new OtherSpellIsCastTrigger() {
                    @Override
                    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
                        return (cardOnStack.getController().getId() == pId && cardOnStack.hasType(MagicType.Creature)) ?
                            new MagicEvent(
                                cardOnStack,
                                new MagicMayChoice(),
                                this,
                                "PN may\$ search his or her library for a creature card, put it onto the battlefield, then shuffle his or her library."
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
