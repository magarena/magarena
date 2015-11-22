[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.isSource(permanent.getEquippedCreature()) && damage.isTargetPlayer() && damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "PN puts a 2/2 green Wolf creature token onto the battlefield and " +
                    "RN puts the top ten cards of his or her library into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(event.getPlayer(),CardDefinitions.getToken("2/2 green Wolf creature token")));
            game.doAction(new MillLibraryAction(event.getRefPlayer(),10));
        }
    }
]
