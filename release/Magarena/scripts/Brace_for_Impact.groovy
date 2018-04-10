def preventAddPlus = new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
        final int amount = (damage.getTarget() == permanent) ? damage.prevent() : 0;
        return amount > 0 ?
            new MagicEvent(
                permanent,
                amount,
                this,
                "Prevent RN damage and put RN +1/+1 counters on SN."
            ):
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeCountersAction(
            event.getPlayer(),
            event.getPermanent(),
            MagicCounterType.PlusOne,
            event.getRefInt()
        ));
    }
};

def POS_TARGET_MULTICOLORED_CREATURE = MagicTargetChoice.Positive("target multicolored creature");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_MULTICOLORED_CREATURE,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent all damage that would be dealt to target multicolored creature\$ this turn. " +
                "For each 1 damage prevented this way, put a +1/+1 counter on that creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddTurnTriggerAction(it, preventAddPlus));
            });
        }
    }
]
