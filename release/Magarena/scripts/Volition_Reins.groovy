[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.getEnchantedPermanent().isTapped() ? new MagicEvent(
                permanent,
                permanent.getEnchantedPermanent(),
                this,
                "PN untaps RN."
            ) :
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new UntapAction(event.getRefPermanent()));
        }
    }
]
