[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{X}"))
                ),
                this,
                "PN may pay\$ {X}\$. If PN does, he or she creates X 1/1 red and white Goblin Soldier creature tokens."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 red and white Goblin Soldier creature token"),
                    event.getPaidMana().getX()
                ));
            }
        }
    }
]
