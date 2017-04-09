package magic.data.stats.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import magic.utility.MagicResources;
import org.h2.tools.RunScript;

enum H2Schema {

    /**
     * Names of schema script files (minus the .sql extension).
     *
     * Schema scripts are stored in /resources/h2/stats/
     *
     * !! DO NOT CHANGE ORDER ONCE RELEASED !!
     * Scripts are run in the order shown (ie. enum ordinal).
     *
     */

    schema_0     // initial schema.
    ;


    private static H2Schema getCurrentSchema(Connection conn) throws SQLException {
        String SQL = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'GAMESTATS_SETTINGS'";
        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next() == false) {
                return null;
            }
        }
        SQL = "SELECT SCHEMA_VERSION FROM GAMESTATS_SETTINGS";
        try (PreparedStatement ps = conn.prepareStatement(SQL)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return H2Schema.valueOf(rs.getString(1));
            } else {
                return null;
            }
        }
    }

    static void applySchemaUpdates(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        H2Schema currentSchema = getCurrentSchema(conn);
        int start = currentSchema == null ? 0 : currentSchema.ordinal() + 1;
        for (int i = start; i < H2Schema.values().length; i++) {
            H2Schema schema = H2Schema.values()[i];
            RunScript.execute(conn, MagicResources.getH2ScriptFile(schema.name() + ".sql"));
            String SQL = i == 0
                ? "INSERT INTO GAMESTATS_SETTINGS (SCHEMA_VERSION) VALUES (?)"
                : "UPDATE GAMESTATS_SETTINGS SET SCHEMA_VERSION = ?";
            try (PreparedStatement ps = conn.prepareStatement(SQL)) {
                ps.setString(1, schema.name());
                ps.executeUpdate();
            }
            conn.commit();
        }
    }
}
