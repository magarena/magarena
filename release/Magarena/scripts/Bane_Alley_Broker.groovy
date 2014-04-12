def HAS_EXILED_BEFORE_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicPermanent permanent = (MagicPermanent)source;
        return permanent.getExiledCards().size() > 0;
    }
};

def public class ExileCardFromHandAction extends MagicAction {
    private final MagicPermanent source;
    private final MagicCard card;

    public ExileCardFromHandAction(final MagicPermanent source,final MagicCard card){
        this.source = source;
        this.card = card;
    }
    
    public void doAction(final MagicGame game) {
        game.doAction(new MagicRemoveCardAction(card, MagicLocationType.OwnersHand));
        game.doAction(new MagicMoveCardAction(card, MagicLocationType.OwnersHand, MagicLocationType.Exile));
        source.addExiledCard(card);
    }
    
    public void undoAction(final MagicGame game) {
        source.removeExiledCard(card);
    }
};

def public class ReclaimExiledCardAction extends MagicAction {
    private final MagicPermanent source;
    private final MagicCard card;

    public ReclaimExiledCardAction(final MagicPermanent source,final MagicCard card){
        this.source = source;
        this.card = card;
    }
    
    public void doAction(final MagicGame game) {
        game.doAction(new MagicRemoveCardAction(card, MagicLocationType.Exile));
        game.doAction(new MagicMoveCardAction(card, MagicLocationType.Exile, MagicLocationType.OwnersHand));
        source.removeExiledCard(card);
    }
    
    public void undoAction(final MagicGame game) {
        source.addExiledCard(card);
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw & Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                0,
                this,
                "PN draws a card, then exiles a card from his or her hand."
            );
        }       
        public MagicEvent exileCardFromHandEvent(final MagicPermanent source) {
            return new MagicEvent(
                source,
                MagicTargetChoice.A_CARD_FROM_HAND,
                MagicGraveyardTargetPicker.ExileOwn,
                1,
                this,
                "PN exiles a card\$ from his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefInt() == 0) {
               game.doAction(new MagicDrawAction(event.getPlayer(),1));
               game.addEvent(exileCardFromHandEvent(event.getPermanent()));
            }
            if (event.getRefInt() == 1) {
                event.processTargetCard(game, {
                    final MagicCard card ->
                    game.doAction(new ExileCardFromHandAction(event.getPermanent(),card));
                });
            }
        }
    },
    new MagicPermanentActivation(
        [HAS_EXILED_BEFORE_CONDITION],
        new MagicActivationHints(MagicTiming.Draw),
        "Reclaim"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{U}{B}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicFromCardListChoice(permanent.getExiledCards(),1),
                this,
                "PN returns a card\$ exiled with Bane Alley Broker to its owner's hand."
            );
        }       
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processChosenCards(game, {
                final MagicCard card ->
                game.doAction(new ReclaimExiledCardAction(event.getPermanent(),card));
            });
        }
    }
]
