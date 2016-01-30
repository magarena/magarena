def preventAddPlus = new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
        int amount = 0;

        if (damage.getTarget() == permanent) {
            amount = damage.prevent();
        }

        return amount > 0 ?
            new MagicEvent(
                permanent,
                {
                    final MagicGame G, final MagicEvent E ->
                        G.doAction(new ChangeCountersAction(
                            E.getPermanent(),
                            MagicCounterType.PlusOne,
                            amount
                        ));
                },
                "Prevent ${amount} damage and put ${amount} +1/+1 counters on SN"
            ) :
            MagicEvent.NONE;
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
