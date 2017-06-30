[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_PLAYER,
                this,
                "Exile all cards from target player's\$ graveyard."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList graveyard = new MagicCardList(it.getGraveyard());
                for (final MagicCard cardGraveyard : graveyard) {
                    game.doAction(new ShiftCardAction(cardGraveyard, MagicLocationType.Graveyard, MagicLocationType.Exile));
                }
            });
        }
    },
    
    new OtherEntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.hasType(MagicType.Enchantment) && otherPermanent.isFriend(permanent) && otherPermanent != permanent) ?
                new MagicEvent(
                    permanent,
                    NEG_TARGET_PLAYER,
                    this,
                    "Exile all cards from target player's\$ graveyard."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final MagicCardList graveyard = new MagicCardList(it.getGraveyard());
                for (final MagicCard cardGraveyard : graveyard) {
                    game.doAction(new ShiftCardAction(cardGraveyard, MagicLocationType.Graveyard, MagicLocationType.Exile));
                }
            });
        }
    }
]
