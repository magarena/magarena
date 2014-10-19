def GREEN_CREATURE=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasColor(MagicColor.Green) && target.isCreature();
    } 
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN gains X + 1 life, where X is the number of green creatures in play."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(), game.getNrOfPermanents(GREEN_CREATURE) + 1));
        }
    }
]
