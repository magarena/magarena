[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource()==permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_CARD_FROM_OPPONENTS_GRAVEYARD
                    ),
                    MagicGraveyardTargetPicker.PutOntoBattlefield,
                    this,
                    "PN may\$ cast target instant or sorcery card\$ from your opponent's graveyard without paying its mana cost. "+
                    "If that card would be put into a graveyard this turn, exile it instead."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                        final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,event.getPlayer(),MagicPayedCost.NO_COST);
                        cardOnStack.setMoveLocation(MagicLocationType.Exile);
                        game.doAction(new MagicPutItemOnStackAction(cardOnStack));
                    }
                });
            }
        }
    }
]
