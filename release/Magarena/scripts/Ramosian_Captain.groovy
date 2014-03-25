def REBEL_CMC_4_OR_LESS = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Rebel) && target.getConvertedCost() <= 4;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Library;
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Search"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{5}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for an Rebel permanent card with converted mana cost 4 or less and puts that card onto the battlefield. Then shuffle PN's library."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicTargetChoice(REBEL_CMC_4_OR_LESS, "a Rebel permanent card with converted mana cost 4 or less")    
            ));
        }
    }
]