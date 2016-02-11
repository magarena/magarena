[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return (died != permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals 1 damage to each creature and each player."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            CREATURE.filter(event) each {
                game.doAction(new DealDamageAction(source, it, 1));
            }
            game.getAPNAP() each {
                game.doAction(new DealDamageAction(source, it, 1));
            }
        }
    }
]
