[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Destroy all artifacts. They can't be regenerated. The controller of each artifact destroyed this way gains life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),ARTIFACT);
            for (final MagicPermanent target : targets) {
                game.doAction(MagicChangeStateAction.Set(target,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicChangeLifeAction(target.getController(),target.getConvertedCost()));
            }
            game.doAction(new MagicDestroyAction(targets));
        }
    }
]
