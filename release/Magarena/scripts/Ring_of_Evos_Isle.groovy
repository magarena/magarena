[
    new MagicPermanentActivation(
        [MagicCondition.HAS_EQUIPPED_CREATURE],
        new MagicActivationHints(MagicTiming.Pump),
        "Hexproof"
    ){
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getEquippedCreature(),
                this,
                "RN gains hexproof until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicGainAbilityAction(event.getRefPermanent(),MagicAbility.Hexproof));
        }
    },
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent equipped = permanent.getEquippedCreature();
            return equipped.isValid() && equipped.hasColor(MagicColor.Blue) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a +1/+1 counter on creature equipped by SN if it's blue."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent equipped=permanent.getEquippedCreature();
            if (equipped.isValid() && equipped.hasColor(MagicColor.Blue)) {
                game.doAction(new ChangeCountersAction(equipped,MagicCounterType.PlusOne,1));
            }
        }
    }
]
