[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Attach all Equipment on the battlefield to SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            EQUIPMENT.filter(event) each {
                game.doAction(new AttachAction(it,event.getPermanent()));
            }
        }
    }
]
