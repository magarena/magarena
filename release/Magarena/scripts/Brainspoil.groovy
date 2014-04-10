def CREATURE_THAT_IS_NOT_ENCHANTED = new MagicPermanentFilterImpl(){
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isCreature() &&
                   !target.isEnchanted();
    }
};

def TARGET_CREATURE_THAT_IS_NOT_ENCHANTED = new MagicTargetChoice(
    CREATURE_THAT_IS_NOT_ENCHANTED,
    "target creature, which is not enchanted"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE_THAT_IS_NOT_ENCHANTED,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target white creature\$. It can't be regenerated. PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent targetPermanent ->
                game.doAction(MagicChangeStateAction.Set(targetPermanent,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(targetPermanent));
            });
        }
    }
]
