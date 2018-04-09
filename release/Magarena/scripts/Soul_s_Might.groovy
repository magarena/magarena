[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicPowerTargetPicker.create(),
                this,
                "Put X +1/+1 counters on target creature\$, where X is that creature's power."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeCountersAction(event.getPlayer(),it,MagicCounterType.PlusOne,it.getPower()));
            });
        }
    }
]
