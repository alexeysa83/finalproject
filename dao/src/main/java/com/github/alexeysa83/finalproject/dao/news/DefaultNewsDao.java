package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.MysqlConnection;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.News;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// In this class methods with transactions just for practice
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

    @Override
    public News createAndSave(News news) {
//        ResultSet rs = null;
//        News created = null;
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("insert into news (title, content, creation_time, author_news) values (?, ?,?, ?)  ",
                            Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, news.getTitle());
                statement.setString(2, news.getContent());
                statement.setTimestamp(3, news.getCreationTime());
                statement.setString(4, news.getAuthorNews());
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
                                    news.getCreationTime(), news.getAuthorNews());
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
//        return created;
    }

    @Override
    public News getById(long id) {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("select title, content, creation_time, author_news from news where id = ?")) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    final boolean exist = resultSet.next();
                    if (!exist) {
                        return null;
                    }
                    final String title = resultSet.getString("title");
                    final String content = resultSet.getString("content");
                    final Timestamp creationTime = resultSet.getTimestamp("creation_time");
                    final String authorNews = resultSet.getString("author_news");
                    connection.commit();
                    return new News(id, title, content, creationTime, authorNews);
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

        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("update news set title = ?, content = ? where id = ?")) {
            statement.setString(1, news.getTitle());
            statement.setString(2, news.getContent());
            statement.setLong(3, news.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public boolean update(News news) {
//        Connection connection = null;
//        try {
//            connection = mysql.getConnection();
//            connection.setAutoCommit(false);
//            try (PreparedStatement statement = connection.prepareStatement
//                    ("update news set title = ?, content = ? where id = ?")) {
//                statement.setString(1, news.getTitle());
//                statement.setString(2, news.getContent());
//                statement.setLong(3, news.getId());
//                connection.commit();
//                return statement.executeUpdate() > 0;
//            }
//        } catch (SQLException e) {
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//                throw new RuntimeException(ex);
//            }
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @Override
    public boolean delete(long id) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("delete from news where id = ?")) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public boolean delete(long id) {
//        Connection connection = null;
//        try {
//            connection = mysql.getConnection();
//            connection.setAutoCommit(false);
//            try (PreparedStatement statement = connection.prepareStatement("delete from news where id = ?")) {
//                statement.setLong(1, id);
//                connection.commit();
//                return statement.executeUpdate() > 0;
//            }
//        } catch (SQLException e) {
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//                throw new RuntimeException(ex);
//            }
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    // add page and limit parameters
    @Override
    public List<News> getNewsOnPage() {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("select * from news order by id desc limit 10")) {
                //statement.setInt(1, page); page parameter
                try (ResultSet resultSet = statement.executeQuery()) {
                    final List<News> newsList = new ArrayList<>();
                    while (resultSet.next()) {
                        final long id = resultSet.getLong("id");
                        final String title = resultSet.getString("title");
                        final String content = resultSet.getString("content");
                        final Timestamp creationTime = resultSet.getTimestamp("creation_time");
                        final String authorNews = resultSet.getString("author_news");
                        newsList.add(new News(id, title, content, creationTime, authorNews));
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
}
