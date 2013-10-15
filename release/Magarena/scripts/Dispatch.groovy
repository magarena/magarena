[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target creature\$. " +
                "If you control three or more artifacts, exile it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicTapAction(permanent,true));
					if (MagicCondition.METALCRAFT_CONDITION.accept(event.getSource())) {
                        game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
                    }
                }
            });
        }
    }
]
