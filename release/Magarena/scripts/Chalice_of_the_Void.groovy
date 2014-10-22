[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.Charge,payedCost.getX()));
            return MagicEvent.NONE;
        }
    },
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                cardOnStack,
                this,
                "Counter RN if its converted mana cost is equal to the number of charge counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefCardOnStack().getConvertedCost() == event.getPermanent().getCounters(MagicCounterType.Charge)) {
                game.doAction(new MagicCounterItemOnStackAction(event.getRefCardOnStack()));
            }
        }
    }
]
