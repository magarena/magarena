package magic.model.player;

public interface IPlayerProfileListener {
    void playerProfileUpdated(final PlayerProfile player);
    void PlayerProfileDeleted(final PlayerProfile player);
    void PlayerProfileSelected(final PlayerProfile player);
}
