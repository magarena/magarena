[
    new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer controller=creature.getController();
            return (controller!=permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    controller,
                    this,
                    "PN loses 1 life."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-1));
        }
    }
]
