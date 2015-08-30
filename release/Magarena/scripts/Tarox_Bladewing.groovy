def CARD_NAMED_TAROX_BLADEWING = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equals("Tarox Bladewing");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
}; 
def A_CARD_NAMED_TAROX_BLADEWING = new MagicTargetChoice(
    CARD_NAMED_TAROX_BLADEWING,  
    MagicTargetHint.None,
    "a card named Tarox Bladwing from your hand"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Grandeur"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicDiscardChosenEvent(source,A_CARD_NAMED_TAROX_BLADEWING)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN gets +X/+X until end of turn, where X is its power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final int power=permanent.getPower();
            game.doAction(new ChangeTurnPTAction(permanent,power,power));
        }
    }
]
