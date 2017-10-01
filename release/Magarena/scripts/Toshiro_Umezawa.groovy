def choice = MagicTargetChoice.Positive("target instant card from your graveyard")

[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isEnemy(permanent) && otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        choice,
                    ),
                    MagicGraveyardTargetPicker.ReturnToHand,
                    this,
                    "PN may\$ cast target instant card\$ from his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game, {
                    game.doAction(new CastCardAction(
                        event.getPlayer(),
                        it,
                        MagicLocationType.Graveyard,
                        MagicLocationType.Exile
                    ));
                });
            }
        }
    }
]
