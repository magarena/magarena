def LAND_IN_GRAVEYARD = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Land);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Graveyard;
    }
};
    
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN puts a 1/1 black and green Worm creature token onto the battlefield for each land card in his or her graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = game.filterCards(event.getPlayer(),LAND_IN_GRAVEYARD).size();
            if(amount > 0) {
                game.doAction(new MagicPlayTokensAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("1/1 black and green Worm creature token"),
                    amount
                ));
            }
        }
    }
]
