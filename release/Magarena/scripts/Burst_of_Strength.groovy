[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicTapTargetPicker.Untap,
                this,
                "PN puts a +1/+1 counter on target creature\$. Untap it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1,true));
                game.doAction(new MagicUntapAction(creature));
            });
        }
    }
]
