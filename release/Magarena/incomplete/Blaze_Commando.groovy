// should only trigger once for each "deals", regardless of how many players or permanents are dealt damage

[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource dmgSource = damage.getSource();
            return (permanent.isFriend(dmgSource) && 
                    dmgSource.isSpell() &&
                    dmgSource.getCardDefinition().isSpell()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts two 1/1 red and white Soldier creature token with haste onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                TokenCardDefinitions.get("RW Soldier"),
                2
            ));
        }
    }
]
