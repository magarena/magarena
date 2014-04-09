[
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (damage.getSource() == permanent &&
                    permanent.isOpponent(damage.getTarget()) &&
                    damage.isCombat()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
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
            for(int i = 0; i < amount; ) {
                if(player.getLibrary().size() <= 0){ i=amount; }
                else {
                    if(player.getLibrary().getCardAtTop().hasType(MagicType.Land)){ i++; }
                    game.doAction(new MagicMillLibraryAction(player,1));
                }
            }
        }
    }
]
