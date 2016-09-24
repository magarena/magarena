def TEXT1 = "PN draws five cards and loses 5 life."

def TEXT2 = "PN puts an X/X black Demon creature token with flying onto the battlefield, "+
            "where X is the number of cards in his or her hand as the token enters the battlefield."

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.isKicked() ?
                    MagicChoice.NONE :
                    new MagicOrChoice(
                        MagicChoice.NONE,
                        MagicChoice.NONE
                    ),
                this,
                payedCost.isKicked() ?
                    TEXT1 + " " + TEXT2 :
                    "Choose one\$ — (1) " + TEXT1 + " (2) " + TEXT2
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isKicked() || event.isMode(1)) {
                game.doAction(new DrawAction(event.getPlayer(),5));
                game.doAction(new ChangeLifeAction(event.getPlayer(),-5));
            }
            if (event.isKicked() || event.isMode(2)) {
                final MagicPlayer player = event.getPlayer();
                final int amount = player.getHandSize();
                game.logAppendValue(player,amount);
                game.doAction(new PlayTokenAction(
                    player,
                    CardDefinitions.getToken(amount, amount, "black Demon creature token with flying")
                ));
            }
        }
    }
]
