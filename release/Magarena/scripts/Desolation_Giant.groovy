[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                payedCost.getKicker(),
                this,
                payedCost.isKicked() ?
                    "Destroy all creatures other than SN." :
                    "Destroy all creatures you control other than SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getRefInt() == 1) {
                game.doAction(new DestroyAction(
                    CREATURE.except(event.getPermanent()).filter(event)
                ));
            } else {
                game.doAction(new DestroyAction(
                    CREATURE_YOU_CONTROL.except(event.getPermanent()).filter(event)
                ));
            }
        }
    }
]
