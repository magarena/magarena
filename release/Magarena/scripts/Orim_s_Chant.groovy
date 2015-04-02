[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "Target player\$ can't cast spells this turn. If SN was kicked, creatures can't attack this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new MagicChangePlayerStateAction(it, MagicPlayerState.CantCastSpells));
                if (event.isKicked()) {
                    final Collection<MagicPermanent> creatures = game.filterPermanents(MagicTargetFilterFactory.CREATURE);
                    for (final MagicPermanent creature : creatures) {
                        game.doAction(new MagicGainAbilityAction(creature, MagicAbility.CannotAttack));
                    }
                }
            });
        }
    }
]
