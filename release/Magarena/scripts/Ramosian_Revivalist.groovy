def REBEL_CMC_5_OR_LESS_FROM_GRAVEYARD = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Rebel) && target.getConvertedCost() <= 5;
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType==MagicTargetType.Graveyard;
    }
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Main),
        "Search"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{6}")];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicTargetChoice(REBEL_CMC_5_OR_LESS_FROM_GRAVEYARD, "a Rebel permanent card with converted mana cost 5 or less from your graveyard."),
                MagicGraveyardTargetPicker.PutOntoBattlefield,
                this,
                "PN returns target\$ Rebel permanent card with converted mana cost 5 or less and puts that card onto the battlefield. Then shuffle PN's library."
            );
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard card ->
                game.doAction(new MagicReanimateAction(
                    card,
                    event.getPlayer()
                ));
            });
        }
    }
]
