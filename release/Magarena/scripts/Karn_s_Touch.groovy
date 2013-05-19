def PT = Karn__Silver_Golem.getPT();
def ST = Karn__Silver_Golem.getST();
def TP = Karn__Silver_Golem.getTP();

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_NONCREATURE_ARTIFACT,
                TP,
                this,
                "Target noncreature artifact\$ becomes an artifact creature with " + 
                "power and toughness each equal to its converted mana cost until end of turn"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicBecomesCreatureAction(creature,PT,ST));
                }
            });
        }
    }
]
