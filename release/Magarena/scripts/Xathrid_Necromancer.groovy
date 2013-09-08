[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent diedPermanent) {
            return (permanent == diedPermanent || 
                    (diedPermanent.hasSubType(MagicSubType.Human) &&
                     diedPermanent.isCreature() &&
                     permanent.isFriend(diedPermanent))) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a 2/2 black Zombie creature token onto the battlefield tapped."
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
