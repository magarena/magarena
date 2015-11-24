[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {  
            return cardOnStack.hasType(MagicType.Creature) ?
                new MagicEvent(
                    permanent,
                    cardOnStack,
                    this,
                    "Each player returns all cards with the same name as (${cardOnStack.getName()}) from his or her graveyard to the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final String name = event.getRefCardOnStack().getName();
            for (final MagicPlayer player : game.getAPNAP()) {
                final List<MagicCard> graveyard = cardName(name).from(MagicTargetType.Graveyard).filter(player);
                for (final MagicCard card : graveyard) {
                    game.doAction(new ReanimateAction(card, player));
                }
            }
        }
    }
]
