[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PERMANENT,
                new MagicTapTargetPicker(true, false),
                this,
                "Tap target permanent\$. It doesn't untap during its controller's next untap step."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicTapAction(creature,true));
                    game.doAction(new MagicChangeStateAction(
                        creature,
                        MagicPermanentState.DoesNotUntapDuringNext,
                        true
                    ));
                }
            });
        }
    },
    new MagicPlaneswalkerActivation(-2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Draw a card for each tapped creature target player\$ controls."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    final int amt = game.filterPermanents(player, MagicTargetFilter.TARGET_TAPPED_CREATURE_YOU_CONTROL).size();
                    game.doAction(new MagicDrawAction(event.getPlayer(),amt));
                }
            });
        }
    },
    new MagicPlaneswalkerActivation(-8) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gets an emblem with " + 
                "\"You have no maximum hand size\" and " + 
                "\"Whenever a card is put into your graveyard from anywhere, you may return it to your hand.\""
            );
        }
        @Override
        public void executeEvent(
                final MagicGame outerGame,
                final MagicEvent outerEvent,
                final Object[] outerChoiceResults) {
            final MagicPlayer you = outerEvent.getPlayer();
            outerGame.doAction(new MagicAddStaticAction(
                new MagicStatic(MagicLayer.Player) {
                    @Override
                    public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
                        if (player.getId() == you.getId()) {
                            player.noMaxHandSize();
                        }
                    }
                }
            ));
            outerGame.doAction(new MagicAddTriggerAction(
                new MagicWhenOtherPutIntoGraveyardTrigger() {
                    @Override
                    public MagicEvent executeTrigger(
                            final MagicGame game,
                            final MagicPermanent permanent,
                            final MagicGraveyardTriggerData triggerData) {
                        return triggerData.card.getOwner().getId() == you.getId() ?
                            // HACK: As emblem is not represented, source of event is the card
                            new MagicEvent(
                                triggerData.card, 
                                new MagicMayChoice(),
                                this,
                                "PN may return SN to your hand."
                            ):
                            MagicEvent.NONE
                    }
                    
                    @Override
                    public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object[] choiceResults) {
                        if (MagicMayChoice.isYesChoice(choiceResults[0]) && event.getPlayer().getGraveyard().contains(event.getCard())) {
                            game.doAction(new MagicRemoveCardAction(event.getCard(),MagicLocationType.Graveyard));
                            game.doAction(new MagicMoveCardAction(event.getCard(),MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                        }
                    }
                }
            ));
        }
    }
]
