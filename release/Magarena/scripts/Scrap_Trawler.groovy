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
    new OtherDiesTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
            return permanent == died || (died.isArtifact() && died.isFriend(permanent));
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
            final int cmc = died.getConvertedCost();
            return new MagicEvent(
                permanent,
                new MagicTargetChoice(
                    ARTIFACT_CARD_IN_YOUR_GRAVEYARD_WITH_CMC_LESS_THAN(cmc),
                    "target artifact card in your graveyard with converted mana cost less than ${cmc}"
                ),
                this,
                "Return target artifact card\$ in PN's graveyard with converted mana cost less than ${cmc} to PN's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
            });
        }
    }
]
