[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ARTIFACT,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target artifact\$. It can't be regenerated. Its controller gains life equal to its converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(MagicChangeStateAction.Set(permanent,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(permanent));
                game.doAction(new MagicChangeLifeAction(permanent.getController(),permanent.getConvertedCost()));
            });
        }
    }
]
