[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (source.isCreature() && source.isFriend(permanent) &&
                    damage.isCombat() && damage.isTargetPlayer()) ?
                new MagicEvent(
                    permanent,
                    source,
                    this,                     
                    "Put a +1/+1 counter on RN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1));        
        }
    },
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{G/U}{G/U}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card for each creature you control with power 4 or greater."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(
                event.getPlayer(),
                event.getPlayer().getNrOfPermanents(CREATURE_POWER_4_OR_MORE)
            ));
        }
    }
]
