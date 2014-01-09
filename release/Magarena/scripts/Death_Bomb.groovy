[
    new MagicAdditionalCost() {
        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return new MagicSacrificePermanentEvent(source, MagicTargetChoice.SACRIFICE_CREATURE);
        }
    },

    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target nonblack creature\$. It can't be regenerated. Its controller loses 2 life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(MagicChangeStateAction.Set(permanent,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new MagicDestroyAction(permanent));
                game.doAction(new MagicChangeLifeAction(permanent.getController(),-2));
            });
        }
    }
]
