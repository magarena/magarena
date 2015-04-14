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
            ARTIFACT.filter(game) each {
                game.doAction(ChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                final DestroyAction act = new DestroyAction(it)
                game.doAction(act);
                if (act.isDestroyed()) {
                    game.doAction(new ChangeLifeAction(it.getController(),it.getConvertedCost()));
                }
            }
        }
    }
]
