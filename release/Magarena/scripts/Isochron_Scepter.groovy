def INSTANT_LEQ_CMC_2_FROM_HAND = new MagicCardFilterImpl() {
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            final MagicCardDefinition cardDefinition = target.getCardDefinition();
            return (cardDefinition.getConvertedCost() <= 2 && target.hasType(MagicType.Instant));
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Hand;
        }
    };
[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final MagicTargetChoice targetChoice = new MagicTargetChoice(
                INSTANT_LEQ_CMC_2_FROM_HAND,  
                MagicTargetHint.None,
                "an instant card with converted mana cost 2 or less to exile from your hand"
            );
            return new MagicEvent(
                permanent,
                new MagicMayChoice(targetChoice),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile an instant card\$ with converted mana cost 2 or less from his or her hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent source = event.getPermanent();
            final MagicCardList exiled = source.getExiledCards();            
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard target) {
                        game.doAction(new MagicRemoveCardAction(target, MagicLocationType.OwnersHand));
                        game.doAction(new MagicMoveCardAction(target, MagicLocationType.OwnersHand,MagicLocationType.Exile));
                        exiled.addToTop(target);
                    }
                });
            }else{
                exiled.addToTop(MagicCard.NONE);
            }
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Copy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostTapEvent(source, "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final MagicCardList exiled = source.getExiledCards();
            final MagicCard card = exiled.getCardAtTop();
            return new MagicEvent(
                source,
                this,
                "PN plays a copy of " + card.getName() + " without paying its mana cost."       
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicCardList exiled = permanent.getExiledCards();
            final MagicCard card = exiled.getCardAtTop();
            final MagicCard copy = MagicCard.createTokenCard(card.getCardDefinition(),event.getPlayer());
            final MagicCardOnStack cardOnStack=new MagicCardOnStack(copy,event.getPlayer(),MagicPayedCost.NO_COST);
            cardOnStack.setMoveLocation(MagicLocationType.Exile);
            game.doAction(new MagicPutItemOnStackAction(cardOnStack));
        }
    }
]
