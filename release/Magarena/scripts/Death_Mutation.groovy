[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
                new MagicDestroyTargetPicker(true),
                this,
                "Destroy target nonblack creature\$. " +
                "It can't be regenerated. " +
                "Put X 1/1 green Saproling creature tokens onto the battlefield, where X is that creature's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                    game.doAction(new MagicDestroyAction(creature));
                    game.doAction(new MagicPlayTokensAction(
                        event.getPlayer(),
                        TokenCardDefinitions.get("Saproling"),
                        creature.getConvertedCost()
                    ));
                }
            });
        }
    }
]
