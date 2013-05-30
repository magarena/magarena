def targetFilter = new MagicTargetFilter.MagicCMCCardFilter(
    MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
    MagicTargetFilter.Operator.LESS_THAN_OR_EQUAL,
    2
);
  
def targetChoice = new MagicTargetChoice(
    targetFilter,false,MagicTargetHint.None,
    "target creature card from your graveyard)"
);

[
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            return (triggerData.fromLocation == MagicLocationType.Play) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(targetChoice),
                    new MagicGraveyardTargetPicker(false),
                    this,
                    "PN may\$ return target creature card\$ with " +
                    "converted mana cost 2 or less " +
                    "from his or her graveyard to the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicReanimateAction(
                            event.getPlayer(),
                            card,
                            MagicPlayCardAction.NONE
                        ));
                    }
                });
            }
        }
    }
]
