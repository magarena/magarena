def ARTIFACT_OR_ENCHANTMENT_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Artifact) || target.hasType(MagicType.Enchantment);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};
 
def AN_ARTIFACT_OR_ENCHANTMENT_FROM_HAND = new MagicTargetChoice(
    ARTIFACT_OR_ENCHANTMENT_FROM_HAND,  
    MagicTargetHint.None,
    "an artifact or enchantment card from your hand"
);

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getOpponent(),
                this,
                "PN may put an artifact or enchantment card onto the battlefield from his or her hand."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(AN_ARTIFACT_OR_ENCHANTMENT_FROM_HAND)
            ));
        }
    }
]
