[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return (cardOnStack.isEnemy(permanent) && cardOnStack.hasType(MagicType.Creature) && enchanted.isValid()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a +1/+1 counter on creature enchanted by SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent=event.getPermanent();
            final MagicPermanent enchanted=permanent.getEnchantedPermanent();
            if (enchanted.isValid()) {
                game.doAction(new ChangeCountersAction(enchanted,MagicCounterType.PlusOne,1));
            }
        }
    }
]
