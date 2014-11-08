[
    new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent tapped) {
            final MagicPermanent enchantedArtifact = permanent.getEnchantedPermanent();
            return (enchantedArtifact.isArtifact() && enchantedArtifact==tapped) ?
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
            game.doAction(new MagicChangePoisonAction(event.getPlayer(),1));
        }
    }
]
