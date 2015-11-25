[
    new BecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            return permanent.getEnchantedPermanent() == tapped ?
                new MagicEvent(
                    permanent,
                    tapped.getController(),
                    this,
                    "PN gets a poison counter."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new ChangePoisonAction(event.getPlayer(),1));
        }
    }
]
