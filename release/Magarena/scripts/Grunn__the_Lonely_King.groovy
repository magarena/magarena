[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            if (payedCost.isKicked()) {
                game.doAction(new ChangeCountersAction(permanent.getController(),permanent,MagicCounterType.PlusOne,5));
            }
            return MagicEvent.NONE;
        }
    },
    new ThisAttacksTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
            return super.accept(permanent, attacker) && attacker.getController().getNrOfAttackers() == 1;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "Double SN's power and toughness until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int power = event.getPermanent().getPower();
            final int toughness = event.getPermanent().getToughness();
            game.doAction(new ChangeTurnPTAction(event.getPermanent(), power, toughness));
        }
    }
]
