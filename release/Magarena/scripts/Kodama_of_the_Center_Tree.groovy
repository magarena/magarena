[
    new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game, final MagicPlayer player, final MagicPowerToughness pt) {
            final int amount = game.filterPermanents(player,MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL).size();
            pt.set(amount, amount);
        }
    },
    new MagicWhenPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
            if (triggerData.fromLocation == MagicLocationType.Play) {
                final int cmc = game.filterPermanents(
                    permanent.getController(),
                    MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL
                ).size()+1;
                final MagicTargetFilter<MagicCard> targetFilter =
                    new MagicTargetFilter.MagicCMCCardFilter(
                        MagicTargetFilter.TARGET_SPIRIT_CARD_FROM_GRAVEYARD,
                        MagicTargetFilter.Operator.LESS_THAN_OR_EQUAL,
                        cmc
                    );
                final MagicTargetChoice targetChoice =
                    new MagicTargetChoice(
                        targetFilter,false,MagicTargetHint.None,
                        "a Spirit card from your graveyard"
                    );
                return new MagicEvent(
                    permanent,
                    new MagicMayChoice(targetChoice),
                    new MagicGraveyardTargetPicker(false),
                    this,
                    "PN may\$ return target Spirit card\$ with " +
                    "converted mana cost " + cmc + " or less " +
                    "from his or her graveyard to his or her hand."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(
                            card,
                            MagicLocationType.Graveyard
                        ));
                        game.doAction(new MagicMoveCardAction(
                            card,
                            MagicLocationType.Graveyard,
                            MagicLocationType.OwnersHand
                        ));
                    }
                });
            }
        }
    }
]
