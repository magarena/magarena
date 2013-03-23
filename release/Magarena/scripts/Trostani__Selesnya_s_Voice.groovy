[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent &&
                    otherPermanent.isCreature() && 
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "PN gains life equal to " + otherPermanent + "'s toughness."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            // get toughness here so counters on the creature are considered
            final int toughness = (event.getRefPermanent()).getToughness();
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),toughness));
        }
    },
    new MagicPermanentActivation(
        [
            MagicConditionFactory.ManaCost("{1}{G}{W}"),
            MagicCondition.CAN_TAP_CONDITION
        ],
        new MagicActivationHints(MagicTiming.Flash),
        "Populate") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostTapEvent(source, source.getController(), MagicManaCost.create("{1}{G}{W}"))];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Populate."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.addEvent(new MagicPopulateEvent(event.getSource()));
        }
    }
]
