[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource dmgSource=damage.getSource();
            return (permanent.isController(damage.getTarget()) && dmgSource.isPermanent()) ?
                new MagicEvent(
                    permanent,
                    dmgSource,
                    this,
                    "Return RN to its owner's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new RemoveFromPlayAction(
                event.getRefPermanent(),
                MagicLocationType.OwnersHand
            ));
        }
    }
]
