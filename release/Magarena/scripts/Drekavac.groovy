def NONCREATURE_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return !target.hasType(MagicType.Creature);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
}; 
def A_NONCREATURE_CARD_FROM_HAND = new MagicTargetChoice(
    NONCREATURE_CARD_FROM_HAND,  
    MagicTargetHint.None,
    "a noncreature card from your hand"
);

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Discard a noncreature card?"),
                this,
                "PN may\$ discard a noncreature card. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent discard = new MagicDiscardChosenEvent(event.getSource(), A_NONCREATURE_CARD_FROM_HAND);
            if (event.isYes() && discard.isSatisfied()) {
                game.addEvent(discard);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
