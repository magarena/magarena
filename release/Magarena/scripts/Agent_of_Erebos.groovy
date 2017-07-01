[
    new OtherEntersBattlefieldTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
            return permanent == played || (played.hasType(MagicType.Enchantment) && player.isFriend(permanent);
        }

        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
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
    }
]
