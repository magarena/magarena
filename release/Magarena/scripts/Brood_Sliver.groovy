[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (damage.isCombat() &&
                    damage.isTargetPlayer() &&
                    source.hasSubType(MagicSubType.Sliver)) ?
                new MagicEvent(
                    permanent,
                    source.getController(),
                    new MagicSimpleMayChoice(),
                    this,
                    "PN may\$ create a 1/1 colorless Sliver creature token."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 colorless Sliver creature token")
                ));
            }
        }
    }
]
