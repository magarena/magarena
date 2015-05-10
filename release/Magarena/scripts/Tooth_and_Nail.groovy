def TEXT1 = "Search your library for up to two creature cards, reveal them, put them into your hand, then shuffle your library."

def TEXT2 = "Put up to two creature cards from your hand onto the battlefield."

def choice = new MagicTargetChoice("a creature card from your hand");

def effect1 = MagicRuleEventAction.create(TEXT1);

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
                game.addEvent(effect1.getEvent(event));
            } 
            
            if (event.isKicked() || event.isMode(2)) {
                game.addEvent(new MagicPutOntoBattlefieldEvent(
                    event,
                    new MagicMayChoice(choice)
                ));
                game.addEvent(new MagicPutOntoBattlefieldEvent(
                    event,
                    new MagicMayChoice(choice)
                ));
            }
        }
    }
]
