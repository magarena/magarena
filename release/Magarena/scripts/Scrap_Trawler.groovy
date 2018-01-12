def ARTIFACT_CARD_IN_YOUR_GRAVEYARD_WITH_CMC_LESS_THAN = {
    final int cmc ->
    return new MagicCardFilterImpl() {
        @Override
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasType(MagicType.Artifact) && target.getConvertedCost() < cmc;
        }
        @Override
        boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Graveyard;
        }
    };
}

[
    new ThisPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MoveCardAction moveAction) {
            final int cmc = moveAction.card.getConvertedCost();
            return (moveAction.fromLocation == MagicLocationType.Battlefield) ?
                new MagicEvent(
                    permanent,
                    new MagicFromCardFilterChoice(
                        ARTIFACT_CARD_IN_YOUR_GRAVEYARD_WITH_CMC_LESS_THAN(cmc),
                        1,
                        false,
                        "artifact card in your graveyard with converted mana cost less than ${cmc}"
                    ),
                    this,
                    "Return target artifact card\$ in PN's graveyard with converted mana cost less than ${cmc} to PN's hand."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
            });
        }
    }
    ,
    new OtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MoveCardAction moveAction) {
            final int cmc = moveAction.card.getConvertedCost();
            return (moveAction.fromLocation == MagicLocationType.Battlefield) ?
                new MagicEvent(
                    permanent,
                    new MagicFromCardFilterChoice(
                        ARTIFACT_CARD_IN_YOUR_GRAVEYARD_WITH_CMC_LESS_THAN(cmc),
                        1,
                        false,
                        "artifact card in your graveyard with converted mana cost less than ${cmc}"
                    ),
                    this,
                    "Return target artifact card\$ in PN's graveyard with converted mana cost less than ${cmc} to PN's hand."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
            });
        }
    }
]

