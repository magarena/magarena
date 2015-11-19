[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getX(),
                this,
                "Put X white Angel creature tokens with flying onto the battlefield. (X=RN)"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokensAction(
                event.getPlayer(),
                CardDefinitions.getToken("4/4 white Angel creature token with flying"),
                event.getRefInt()
            ));
        }
    },
    new MagicWhenCycleTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
            return new MagicEvent(
                card,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{X}"))
                ),
                this,
                "PN may\$ pay {X}\$. If PN does, he or she puts X 1/1 white Soldier creature tokens onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokensAction(
                    event.getPlayer(),
                    CardDefinitions.getToken("1/1 white Soldier creature token"),
                    event.getPaidMana().getX()
                ));
            }
        }
    }
]
