def ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Creature) ||
               target.hasType(MagicType.Enchantment) ||
               target.hasType(MagicType.Artifact);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Graveyard;
    }
};
def AN_ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD = new MagicTargetChoice(
    ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD,
    MagicTargetHint.Positive,
    "an artifact, creature or enchantment card"
);
[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return (permanent==attacker) ? 
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        AN_ARTIFACT_OR_CREATURE_OR_ENCHANTMENT_CARD_FROM_GRAVEYARD
                    ),
                    MagicGraveyardTargetPicker.ReturnToHand,
                    this,
                    "PN may\$ return target artifact, creature or enchantment card\$ " + 
                    "from his graveyard to his hand."
                ): 
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    final MagicCard card ->
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                });
            }
        }
    }
]
