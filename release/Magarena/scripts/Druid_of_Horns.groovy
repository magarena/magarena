[
    new OtherSpellIsCastTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return cardOnStack.isTarget(permanent) &&
                cardOnStack.isFriend(permanent) &&
                cardOnStack.hasSubType(MagicSubType.Aura);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return new MagicEvent(
                permanent,
                this,
                "PN creates a 3/3 green Beast creature token."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("3/3 green Beast creature token")
            ));
        }
    }
]
