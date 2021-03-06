package ocd.dao.oracle.dao;

import ocd.OCDConsole;
import ocd.dao.interfaces.AdventurerDAO;
import ocd.dao.oracle.OracleDAOFactory;
import ocd.dao.entities.Adventurer;
import ocd.dao.entities.Lord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t00191774 on 16/11/2016.
 *
 */
public class OracleAdventurerDAO implements AdventurerDAO {
    private Connection con;

    public OracleAdventurerDAO() {
        con = OracleDAOFactory.getConnection();
    }

    @Override
    public boolean buyItem(Adventurer adventurer, int itemID) {
        CallableStatement stm = null;
        try {
            stm = con.prepareCall("{call buyItem(?, ?)}");
            stm.setInt(1, adventurer.getId());
            stm.setInt(2, itemID);
            stm.execute();
            return true;
        } catch (SQLException e) {
            OCDConsole.printlnError(e.getMessage());
        } finally {
            OracleDAOFactory.closeStatement(stm);
        }
        return false;
    }

    @Override
    public boolean sellItem(Adventurer adventurer, int itemID) {
        CallableStatement stm = null;
        try {
            stm = con.prepareCall("{call sellItem(?, ?)}");
            stm.setInt(1, adventurer.getId());
            stm.setInt(2, itemID);
            stm.execute();
            return true;
        } catch (SQLException e) {
            OCDConsole.printlnError(e.getMessage());
        } finally {
            OracleDAOFactory.closeStatement(stm);
        }
        return false;
    }

    @Override
    public boolean nextFight(Adventurer adventurer) {
        CallableStatement stm = null;
        try {
            stm = con.prepareCall("{call nextFight(?)}");
            stm.setInt(1, adventurer.getId());
            stm.execute();
            return true;
        } catch (SQLException e) {
            OCDConsole.printlnError(e.getMessage());
        } finally {
            OracleDAOFactory.closeStatement(stm);
        }
        return false;
    }

    @Override
    public boolean flee(Adventurer adventurer) {
        CallableStatement stm = null;
        try {
            stm = con.prepareCall("{call flee(?)}");
            stm.setInt(1, adventurer.getId());
            stm.execute();
            return true;
        } catch (SQLException e) {
            OCDConsole.printlnError(e.getMessage());
        } finally {
            OracleDAOFactory.closeStatement(stm);
        }
        return false;
    }

    @Override
    public List<Adventurer> getAdventurersOfLord(Lord lord) {
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<Adventurer> results = new ArrayList<>();
        try {
            stm = con.prepareStatement("SELECT * FROM TABLE(getAdventurersOfLord(?))");
            stm.setInt(1, lord.getId());
            rs = stm.executeQuery();
            while (rs.next()) {
                results.add(new Adventurer(
                        rs.getInt("adventurerID"),
                        rs.getString("name"),
                        rs.getInt("entityID"),
                        rs.getInt("dungeonID"),
                        rs.getInt("shopID"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            OCDConsole.printlnError(e.getMessage());
        } finally {
            OracleDAOFactory.closeResultSet(rs);
            OracleDAOFactory.closeStatement(stm);
        }
        return results;
    }

    @Override
    public Adventurer createAdventurer(Lord lord, String name) {
        CallableStatement cStm = null;
        Adventurer result = null;
        try {
            cStm = con.prepareCall("{? = call createAdventurer(?, ?)}");
            cStm.registerOutParameter(1, Types.INTEGER);
            cStm.setInt(2, lord.getId());
            cStm.setString(3, name);
            cStm.execute();
            int returnId = cStm.getInt(1);
            result = find(returnId);
        } catch (SQLException e) {
            OCDConsole.printlnError(e.getMessage());
        } finally {
            OracleDAOFactory.closeStatement(cStm);
        }
        return result;
    }

    @Override
    public Adventurer find(int id) {
        PreparedStatement pStm = null;
        ResultSet rs = null;
        Adventurer result;
        try {
            pStm = con.prepareStatement("SELECT * FROM Adventurer WHERE adventurerID = ?");
            pStm.setInt(1, id);
            rs = pStm.executeQuery();
            if (rs.next()) {
                result = new Adventurer(
                        rs.getInt("adventurerID"),
                        rs.getString("name"),
                        rs.getInt("entityID"),
                        rs.getInt("dungeonID"),
                        rs.getInt("shopID"),
                        ""
                );
                return result;
            }
        } catch (SQLException e) {
            OCDConsole.printlnError(e.getMessage());
        } finally {
            OracleDAOFactory.closeResultSet(rs);
            OracleDAOFactory.closeStatement(pStm);
        }
        return null;
    }
}
