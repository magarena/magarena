def TEXT1 = "You draw five cards and you lose 5 life."

def TEXT2 = "Put an X/X black Demon creature token with flying onto the battlefield, "+ 
            "where X is the number of cards in your hand as the token enters the battlefield."

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
                    "Choose one\$ — • " + TEXT1 + " • " + TEXT2
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isKicked() || event.isMode(1)) {
                game.doAction(new DrawAction(event.getPlayer(),5));
                game.doAction(new ChangeLifeAction(event.getPlayer(),-5));
            }
            if (event.isKicked() || event.isMode(2)) {
                final int x = event.getPlayer().getHandSize();
                game.doAction(new MagicPlayTokenAction(event.getPlayer(), MagicCardDefinition.create({
                    it.setName("Demon");
                    it.setFullName("black Demon creature token with flying");
                    it.setPowerToughness(x, x);
                    it.setColors("b");
                    it.addSubType(MagicSubType.Demon);
                    it.addType(MagicType.Creature);
                    it.setToken();
                    it.setValue(x);
                })));
            }
        }
    }
]
