def filter = {
    final int cost ->
    return new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source, final MagicPlayer player, final MagicPermanent target){
            return target.hasType(MagicType.Artifact) && target.getConvertedCost() == cost;
        }
    }
}

def TARGET_ARTIFACT_X = {
    final int cost ->
    return new MagicTargetChoice(filter(cost), "target artifact with converted mana cost "+cost);
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                TARGET_ARTIFACT_X(amount),
                amount,
                this,
                "Destroy target artifact with converted mana cost X.\$ It can't be regenerated. "+
                "SN deals X damage to that artifact's controller. (X=${amount})"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(ChangeStateAction.Set(permanent, MagicPermanentState.CannotBeRegenerated));
                game.doAction(new DestroyAction(permanent));
                game.doAction(new DealDamageAction(event.getSource(), permanent.getController(), event.getRefInt()))
            });
        }
    }
]
