[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    damage.isCombat() &&
                    damage.getTarget().isPlayer()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 2/2 black Zombie creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayCardAction(
                MagicCard.createTokenCard(
                    TokenCardDefinitions.get("Zombie"),
                    event.getPlayer()
                ),
                [MagicPlayMod.TAPPED]
            ));
        }
    }
]
