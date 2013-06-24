[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all creatures you don't control. They can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
            for (final MagicPermanent target : targets) {
                game.doAction(MagicChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
            }
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
