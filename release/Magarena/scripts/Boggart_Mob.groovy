[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source = damage.getSource();
            return (damage.isCombat() &&
                    damage.isTargetPlayer() &&
                    permanent.isFriend(source) &&
                    source.isPermanent() &&
                    source.hasSubType(MagicSubType.Goblin)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.PLAY_TOKEN,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a 1/1 black Goblin Rogue " +
                    "creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("1/1 black Goblin Rogue creature token")));
            }
        }
    }
]
