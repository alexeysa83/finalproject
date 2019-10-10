package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.MysqlConnection;
import com.github.alexeysa83.finalproject.model.News;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultNewsDao implements NewsDao {

    private MysqlConnection mysql = MysqlConnection.getInstance();

    private static volatile NewsDao instance;

    public static NewsDao getInstance() {
        NewsDao localInstance = instance;
        if (localInstance == null) {
            synchronized (NewsDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultNewsDao();
                }
            }
        }
        return localInstance;
    }

    // In this class methods with transactions just for practice
    // Duplicate code
    // add page and limit parameters to get news on page method

    @Override
    public News createAndSave(News news) {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("insert into news (title, content, creation_time, auth_id) values (?, ?,?, ?)  ",
                            Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, news.getTitle());
                statement.setString(2, news.getContent());
                statement.setTimestamp(3, news.getCreationTime());
                statement.setLong(4, news.getAuthId());
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    final boolean isSaved = generatedKeys.next();
                    if (!isSaved) {
                        return null;
                    }
                    final long id = generatedKeys.getLong(1);
                    connection.commit();
                    return new News
                            (id, news.getTitle(), news.getContent(),
                                    news.getCreationTime(), news.getAuthId(), news.getAuthorNews());
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public News getById(long id) {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("select n.title, n.content, n.creation_time, n.auth_id, au.login from news n " +
                            "join auth_user au on n.auth_id = au.id where n.id = ?")) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    final boolean exist = resultSet.next();
                    if (!exist) {
                        return null;
                    }
                    final String title = resultSet.getString("title");
                    final String content = resultSet.getString("content");
                    final Timestamp creationTime = resultSet.getTimestamp("creation_time");
                    final long authId = resultSet.getLong("auth_id");
                    final String login = resultSet.getString("login");
                    connection.commit();
                    return new News(id, title, content, creationTime, authId, login);
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<News> getNewsOnPage() {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("select n.id, n.title, n.content, n.creation_time, n.auth_id, au.login from news n " +
                            "join auth_user au on n.auth_id = au.id order by n.id desc limit 10")) {
                //statement.setInt(1, page); page parameter
                try (ResultSet resultSet = statement.executeQuery()) {
                    final List<News> newsList = new ArrayList<>();
                    while (resultSet.next()) {
                        final long id = resultSet.getLong("id");
                        final String title = resultSet.getString("title");
                        final String content = resultSet.getString("content");
                        final Timestamp creationTime = resultSet.getTimestamp("creation_time");
                        final long authId = resultSet.getLong("auth_id");
                        final String login = resultSet.getString("login");
                        newsList.add(new News(id, title, content, creationTime, authId, login));
                    }
                    connection.commit();
                    return newsList;
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean update(News news) {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("update news set title = ?, content = ? where id = ?")) {
                statement.setString(1, news.getTitle());
                statement.setString(2, news.getContent());
                statement.setLong(3, news.getId());
                final int i = statement.executeUpdate();
                connection.commit();
                return i > 0;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean delete(long id) {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement("delete from news where id = ?")) {
                statement.setLong(1, id);
                final int i = statement.executeUpdate();
                connection.commit();
                return i > 0;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
