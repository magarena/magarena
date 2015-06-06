[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN searches his or her library for a land card of each basic land type and put them onto the battlefield. "+
                "Then PN shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicSubType basic : MagicSubType.ALL_BASIC_LANDS) {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    new MagicFromCardFilterChoice(
                        card(basic).from(MagicTargetType.Library),
                        1,
                        true,
                        "to put onto the battlefield"
                    )
                ));
            }
        }
    }
]
