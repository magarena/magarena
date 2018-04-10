[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "PN puts a +1/+1 counter on target creature\$. " +
                "Put three +1/+1 counters on that creature instead if a creature died this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = game.getCreatureDiedThisTurn() ? 3 : 1;
                game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.PlusOne,amount));
            });
        }
    }
]
