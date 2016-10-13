[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONBLACK_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target nonblack creature\$. " +
                "It can't be regenerated. " +
                "Create X 1/1 green Saproling creature tokens, where X is that creature's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(ChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new DestroyAction(it));
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 green Saproling creature token"),
                    it.getConvertedCost()
                ));
            });
        }
    }
]
