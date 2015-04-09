[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_UNTAPPED_CREATURE,
                MagicTapTargetPicker.Tap,
                this,
                "Tap target untapped creature\$. That creature deals damage equal to its power to its controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicTapAction(it));
                game.doAction(new MagicDealDamageAction(it,it.getController(),it.getPower()));
            });
        }
    }
]
