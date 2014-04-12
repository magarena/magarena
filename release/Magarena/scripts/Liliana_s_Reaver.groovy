[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTarget(),
                this,
                "RN discards a card and PN puts a 2/2 black Zombie creature token onto the battlefield tapped."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(
                event.getSource(),
                event.getRefPlayer()
            ))
            game.doAction(new MagicPlayCardAction(
                MagicCard.createTokenCard(
                    TokenCardDefinitions.get("2/2 black Zombie creature token"),
                    event.getPlayer()
                ),
                [MagicPlayMod.TAPPED]
            ));
        }
    }
]
