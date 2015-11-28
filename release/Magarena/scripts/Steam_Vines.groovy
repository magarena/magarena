[
    new BecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return enchanted==tapped ?
                new MagicEvent(
                    permanent,
                    tapped.getController(),
                    MagicTargetChoice.Negative("a land"),
                    tapped,
                    this,
                    "Destroy RN and SN deals 1 damage to PN. PN attaches SN to a land of his or her choice\$."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(event.getRefPermanent()));
                game.doAction(new DealDamageAction(event.getSource(),event.getPlayer(),1));
                game.doAction(new AttachAction(event.getPermanent(), it));
            });
        }
    }
]
