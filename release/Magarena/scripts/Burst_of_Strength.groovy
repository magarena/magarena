[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicTapTargetPicker.Untap,
                this,
                "PN puts a +1/+1 counter on target creature\$. Untap it."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeCountersAction(it,MagicCounterType.PlusOne,1));
                game.doAction(new UntapAction(it));
            });
        }
    }
]
