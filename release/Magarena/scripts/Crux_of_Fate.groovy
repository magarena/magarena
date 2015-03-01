def DRAGON_CREATURES=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Dragon) && target.isCreature();
    } 
};

def NON_DRAGON_CREATURES=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasSubType(MagicSubType.Dragon) && target.isCreature();
    } 
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicOrChoice(
                    MagicChoice.NONE,
                    MagicChoice.NONE
                ),
                this,
                "Choose one\$ - destroy all Dragon creatures; " +
                "or destroy all non-Dragon creatures."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(event.isMode(1) ? DRAGON_CREATURES : NON_DRAGON_CREATURES);
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
