def PLAINS_OR_WHITE_PERMANENT = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.hasSubType(MagicSubType.Plains) || target.hasColor(MagicColor.White) &&
               target.isController(player);
    } 
};

def A_PLAINS_OR_WHITE_PERMANENT = new MagicTargetChoice(
    PLAINS_OR_WHITE_PERMANENT,
    "a plains or white permanent you control"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return all Islands to their owners' hands. " +
                "Each player sacrifices a Plains or a white permanent for each white permanent he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            ISLAND.filter(event) each {
                game.doAction(new RemoveFromPlayAction(it, MagicLocationType.OwnersHand));
            }
            game.getAPNAP() each {
                final int amount = it.getNrOfPermanents(MagicColor.White);
                for (int i = 0; i < amount; i++) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(), 
                        it,
                        A_PLAINS_OR_WHITE_PERMANENT
                    ));
                }
            }
        }
    }
]
