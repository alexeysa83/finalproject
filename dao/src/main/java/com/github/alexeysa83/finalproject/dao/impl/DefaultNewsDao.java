package com.github.alexeysa83.finalproject.dao.impl;

import com.github.alexeysa83.finalproject.dao.MysqlConnection;
import com.github.alexeysa83.finalproject.dao.NewsDao;
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

    // creation time may be default
    @Override
    public News createAndSave(News news) {
        ResultSet rs = null;
        News created = null;
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("insert into news (title, content, creation_time, author_id) values (?, ?,?, ?)  ", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, news.getTitle());
            statement.setString(2, news.getContent());
            statement.setTimestamp(3, news.getCreationTime());
            statement.setLong(4, news.getAuthorId());
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                created = new News
                        (id, news.getTitle(), news.getContent(),
                                news.getCreationTime(), news.getAuthorId(), news.getAuthorLogin());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return created;
    }

//    @Override
//    public News getById(long id) {
//        try (Connection connection = mysql.getConnection();
//             PreparedStatement statement = connection.prepareStatement
//                     ("select n.title, n.content, n.creation_time, n.author_id, a.login " +
//                             "from news n join auth_user a on n.author_id = a.id where n.id = ?")) {
//            statement.setLong(1, id);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String title = resultSet.getString("title");
//                    String content = resultSet.getString("content");
//                    Timestamp creationTime = resultSet.getTimestamp("creation_time");
//                    long authorId = resultSet.getLong("author_id");
//                    String authorLogin = resultSet.getString("login");
//                    return new News(id, title, content, creationTime, authorId, authorLogin);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public boolean update(News news) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    // add page parameter
    @Override
    public List<News> getNewsOnPage() {

        final List<News> newsList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("select n.id, n.title, n.content, n.creation_time, n.author_id, a.login " +
                            "from news n join auth_user a on n.author_id = a.id order by n.id desc limit 10")) {
                //statement.setInt(1, page); page parameter
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        final long id = resultSet.getLong("id");
                        final String title = resultSet.getString("title");
                        final String content = resultSet.getString("content");
                        final Timestamp creationTime = resultSet.getTimestamp("creation_time");
                        final long authorId = resultSet.getLong("author_id");
                        final String authorLogin = resultSet.getString("login");
                        newsList.add(new News(id, title, content, creationTime, authorId, authorLogin));
                    }
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return newsList;
    }
}
