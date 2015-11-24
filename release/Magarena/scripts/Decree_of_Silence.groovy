[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isEnemy(cardOnStack) ?
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Counter RN and put a depletion counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new CounterItemOnStackAction(event.getRefCardOnStack()));
            game.doAction(new ChangeCountersAction(event.getPermanent(),MagicCounterType.Depletion,1));
        }
    }
]
