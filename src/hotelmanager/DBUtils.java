package hotelmanager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 * DB tools
 * 
 * @author Petr
 */
public class DBUtils {
    private static final Logger logger = Logger.getLogger(
            DBUtils.class.getName());
    
    /**
     * Close connection alo logs possible errors
     * 
     * @param conn connection to close
     * @param statements statements to close
     */
    public static void closeQuietly(Connection conn, Statement ... statements) {
        for(Statement st : statements) {
            if(st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "Error when closing statement", ex);
                }    
            }
        }
        
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when switching autocommit mode back to true", ex);
            }
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error when closing connection", ex);
            }
        }
    }
    
    /**
     * Rolls back transaction and logs possible error.
     * 
     * @param conn connection
     */
    public static void doRollbackQuietly(Connection conn) {
        if(conn != null) {
            try {
                if(conn.getAutoCommit()) {
                    throw new IllegalArgumentException("Connection is in the autocommit mode!");
                }
                conn.rollback();
            } catch(SQLException ex) {
                logger.log(Level.SEVERE, "Error when doing rollback", ex);
            }
        }
    }

    /**
     * Executes SQL script
     * @param ds datasource
     * @param resource url of sql script to be executed
     * @throws SQLExcpetion if operation fails
     */
    public static void executeSqlScript(DataSource ds, URL scriptUrl) throws SQLException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            for(String sqlStatement : readSqlStatements(scriptUrl)) {
                if(!sqlStatement.trim().isEmpty()) {
                    conn.prepareStatement(sqlStatement).executeUpdate();
                }
            }
        } finally {
            closeQuietly(conn);
        }
    }

    /**
     * Reads SQL statements from file. SQL commands in file must be separated by 
     * a semicolon.
     * 
     * @param url url of file
     * @return array of command  strings
     */
    private static String[] readSqlStatements(URL url) {
        try {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            while(true) {
                int count = reader.read(buffer);
                if(count < 0) {
                    break;
                }
                result.append(buffer, 0, count);
            }
            return result.toString().split(";");
        } catch(IOException e) {
            throw new RuntimeException("Cannot read " + url, e);
        }
    }
    
    /**
     * Extract key from given ResultSet.
     * 
     * @param key resultSet with key
     * @return key from given result set
     * @throws SQLException when operation fails
     */
    public static Long getId(ResultSet key) throws SQLException {
        if (key.getMetaData().getColumnCount() != 1) {
            throw new IllegalArgumentException("Given ResultSet contains more columns");
        }
        if (key.next()) {
            Long result = key.getLong(1);
            if (key.next()) {
                throw new IllegalArgumentException("Given ResultSet contains more rows");
            }
            return result;
        } else {
            throw new IllegalArgumentException("Given ResultSet contain no rows");
        }
    }
    
    /**
     * Check if updates count is one. Otherwise appropriate exception is thrown.
     * 
     * @param count updates count.
     * @param entity updated entity (for includig to error message)
     * @param insert flag if performed operation was insert
     * @throws IllegalEntityException when updates count is zero, so updated entity does not exist
     * @throws ServiceFailureException when updates count is unexpected number
     */
    public static void checkUpdatesCount(int count, Object entity, 
            boolean insert) throws IllegalArgumentException, ServiceFailureException {
        
        if (!insert && count == 0) {
            throw new IllegalArgumentException("Entity " + entity + " does not exist in the db");
        }
        if (count != 1) {
            throw new ServiceFailureException("Internal integrity error: Unexpected rows count in database affected: " + count);
        }
    }
}
