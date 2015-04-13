[
    new MagicPermanentActivation(
        [MagicCondition.HAS_EQUIPPED_CREATURE],
        new MagicActivationHints(MagicTiming.Pump),
        "Regen"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}")
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                permanent.getEquippedCreature(),
                this,
                "Regenerate RN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRegenerateAction(event.getRefPermanent()));
        }
    },
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent equipped = permanent.getEquippedCreature();
            return equipped.isValid() && equipped.hasColor(MagicColor.Black) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a +1/+1 counter on creature equipped by SN if it's black."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent equipped=permanent.getEquippedCreature();
            if (equipped.isValid() && equipped.hasColor(MagicColor.Black)) {
                game.doAction(new ChangeCountersAction(equipped,MagicCounterType.PlusOne,1));
            }
        }
    }
]
