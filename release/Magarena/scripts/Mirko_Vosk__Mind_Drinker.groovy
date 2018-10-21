[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    permanent.isOpponent(damage.getTarget()) &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    damage.getTarget(),
                    this,
                    "RN reveals cards from the top of his or her library until he or she reveals four land cards, then puts those cards into his or her graveyard."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = 4;
            final MagicPlayer player = event.getRefPlayer();
            game.doAction(new MillLibraryUntilAction(player, MagicType.Land, amount));
        }
    }
]
