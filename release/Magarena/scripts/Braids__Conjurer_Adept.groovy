def choice = new MagicTargetChoice("an artifact, creature, or land card from your hand");

[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN may put an artifact, creature, or land card from his or her hand onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.addEvent(new MagicPutOntoBattlefieldEvent(
                    event,
                    new MagicMayChoice(choice)
                ));

        }
    }
]
