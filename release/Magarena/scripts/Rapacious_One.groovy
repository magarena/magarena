[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final int amount = damage.getDealtAmount();
            return (amount > 0 &&
                    damage.getSource() == permanent &&
                    damage.getTarget().isPlayer() &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "Put RN 0/1 colorless Eldrazi Spawn " +
                    "creature tokens onto the battlefield. They have " +
                    "\"Sacrifice this creature: Add {1} to your mana pool.\""
                    ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (int count=event.getRefInt();count>0;count--) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Eldrazi Spawn")));
            }
        }
    }
]
