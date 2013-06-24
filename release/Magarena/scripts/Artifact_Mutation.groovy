[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ARTIFACT,
                new MagicDestroyTargetPicker(true),
                this,
                "Destroy target artifact\$. It can't be regenerated. " +
                "Put X 1/1 green Saproling creature tokens onto the battlefield, where X is that artifact's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(MagicChangeStateAction.Set(
                        permanent,
                        MagicPermanentState.CannotBeRegenerated
                    ));
                    game.doAction(new MagicDestroyAction(permanent));
                    game.doAction(new MagicPlayTokensAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get("Saproling"),
                        permanent.getConvertedCost()
                    ));
                }
            });
        }
    }
]
