package userManagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import userManagement.model.User;



//This DAO class provides CRUD database operation for the table users in the database
public class UserDAO {
	private String jdbcURL = "root@localhost:3306";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "(name, email, country) VALUES " + " (?, ?, ?);";
	
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id = ?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?";
	private static final String UPDATE_USERS_SQL = "update users set name = ?, email = ?, country = ? where id = ?;";
	
	protected Connection getConnection() {
	    Connection connection = null;
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
	    } catch (ClassNotFoundException e) {
	        System.err.println("MySQL JDBC Driver not found.");
	        e.printStackTrace();
	    } catch (SQLException e) {
	        System.err.println("Failed to connect to the database.");
	        e.printStackTrace();
	    }
	    return connection;
	}

	
	//Create or Insert user
	public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USERS_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        }catch (Exception e) {
        	e.printStackTrace();
        }
    }
	//Update user
	public boolean updateUser(User user) throws SQLException{
		boolean rowUpdated;
		try(Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);){
					statement.setString(1, user.getName());
					statement.setString(2, user.getEmail());
					statement.setString(3, user.getCountry());
					statement.setInt(4, user.getId());
					
					rowUpdated = statement.executeUpdate() > 0; 
				}
		return rowUpdated;
	}
	
	//Select user by id
	public User selectUser(int id) throws SQLException {
		User user = null;
		//Establish Connection
		try(Connection connection = getConnection();
				//Create a statement using connect object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);){
				preparedStatement.setInt(1, id);
				System.out.println(preparedStatement);
				//Execute query or update query
				ResultSet rs = preparedStatement.executeQuery();
				
				//Process the ResultSet Object
				while(rs.next()) {
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");
					user = new User(id,name,email,country);
				}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	//Select user
	public List<User> selectAllUsers() throws SQLException {
		List<User> users = new ArrayList<>();
		//Establish Connection
		try(Connection connection = getConnection();
				//Create a statement using connect object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
				System.out.println(preparedStatement);
				//Execute query or update query
				ResultSet rs = preparedStatement.executeQuery();
				
				//Process the ResultSet Object
				while(rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");
					users.add(new User(id,name,email,country));
				}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	//Delete user
	public boolean deleteUser(int id)throws SQLException{
		boolean rowDeleted;
		try(Connection connection= getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);){
			statement.setInt(1,id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

}
