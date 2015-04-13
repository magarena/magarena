def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        game.doAction(new ChangeCountersAction(event.getRefPermanent(),MagicCounterType.PlusOne,1));
    }
}

def event = {
    final MagicPermanent permanent, final MagicPermanent target ->
    return new MagicEvent(
        permanent,
        new MagicSimpleMayChoice(
            MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
            1,
            MagicSimpleMayChoice.DEFAULT_YES
        ),
        target ,
        action,
        "PN may put a +1/+1 counter on RN."
    );
}

[
    new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (creature == enchantedCreature) ?  event(permanent, enchantedCreature) : MagicEvent.NONE;
        }
    },
    new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPermanent enchantedCreature = permanent.getEnchantedPermanent();
            return (creature == enchantedCreature) ? event(permanent, enchantedCreature) : MagicEvent.NONE;
        }
    }
]
