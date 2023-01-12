package me.mrdev.cl.profile;

import me.mrdev.cl.CafeLevel;
import me.mrdev.cl.data.sql.SQLDatabase;
import me.mrdev.cl.data.sql.SQLType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ProfileDataManager {

    private CafeLevel plugin;
    private SQLDatabase database;

    public ProfileDataManager(CafeLevel plugin) {
        this.plugin = plugin;
    }

    public SQLDatabase getDatabase() {
        return database;
    }

    public void loadDB() throws SQLException, ClassNotFoundException {
        YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
        ConfigurationSection details = config.getConfigurationSection("Database-details");
        database = new SQLDatabase(details.getString("host") , details.getInt("port") , details.getString("name"), details.getString("user") , details.getString("password"), details.getString("type").equalsIgnoreCase("mysql") ? SQLType.MYSQL : SQLType.SQLite);
        database.connect();
        database.createTable("Profiles" , "uuid TEXT(36),level INT,xp DOUBLE");
    }

    public ArrayList<PlayerProfile> loadProfiles() throws SQLException {
        if(database.isConnected()) {
            ArrayList<PlayerProfile> list = new ArrayList<>();
            ResultSet set = database.executeQuery("SELECT * FROM Profiles");
            while (set.next()) {
                list.add(new PlayerProfile(UUID.fromString(set.getString("uuid")) , set.getInt("level") , set.getDouble("xp")));
            }
            set.close();
            return list;
        }
        return new ArrayList<>();
    }

    public void addProfile(PlayerProfile profile) throws SQLException {
        if(database.isConnected()) {
            database.addRow("Profiles" , "\"" + profile.getID().toString() + "\"," + profile.getLevel() + "," + profile.getXp());
        }
    }

    public void updateProfile(PlayerProfile profile) throws SQLException {
        if(database.isConnected()) {
            database.executeUpdate("UPDATE Profiles SET level=" + profile.getLevel() + ",xp=" + profile.getXp() + "WHERE uuid=\"" + profile.getID() + "\"");
        }
    }


}
