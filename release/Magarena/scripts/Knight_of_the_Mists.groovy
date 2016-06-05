def choice = MagicTargetChoice.Negative("target Knight");

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicOrChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{U}")),
                    choice
                ),
                this,
                "Choose one\$ — (1) Pay {U}. (2) Destroy target Knight\$ and it can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isMode(2)) {
                event.processTargetPermanent(game, {
                    game.doAction(ChangeStateAction.Set(it, MagicPermanentState.CannotBeRegenerated));
                    game.doAction(new DestroyAction(it));
                });
            }
        }
    }
]
