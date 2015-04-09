def NONBLUE_CREATURE=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isCreature() && !target.hasColor(MagicColor.Blue);
    }
};
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all nonblue creatures to their owners' hands."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            NONBLUE_CREATURE.filter(game) each {
                game.doAction(new MagicRemoveFromPlayAction(it,MagicLocationType.OwnersHand));
            }
        }
    }
]
