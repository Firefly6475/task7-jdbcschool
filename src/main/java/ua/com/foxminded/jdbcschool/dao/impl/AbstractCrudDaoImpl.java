package ua.com.foxminded.jdbcschool.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import ua.com.foxminded.jdbcschool.dao.CrudDao;
import ua.com.foxminded.jdbcschool.dao.DBConnector;
import ua.com.foxminded.jdbcschool.dao.Page;
import ua.com.foxminded.jdbcschool.dao.exception.DBRuntimeException;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {
    private static final BiConsumer<PreparedStatement, String> STRING_CONSUMER =
            ((PreparedStatement pr, String param) -> {
                try {
                    pr.setString(1, param);
                } catch (SQLException e) {
                    throw new DBRuntimeException(e);
                }
            });

    private static final BiConsumer<PreparedStatement, Integer> INT_CONSUMER =
            ((PreparedStatement pr, Integer param) -> {
                try {
                    pr.setInt(1, param);
                } catch (SQLException e) {
                    throw new DBRuntimeException(e);
                }
            });

    private final DBConnector connector;
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String findAllPagedQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    public AbstractCrudDaoImpl(DBConnector connector, String saveQuery, String findByIdQuery,
            String findAllQuery, String findAllPagedQuery, String updateQuery, String deleteByIdQuery) {
        this.connector = connector;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.findAllPagedQuery = findAllPagedQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public void save(E entity) {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(saveQuery)) {
            insert(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBRuntimeException("Insertion is failed", e);
        }
    }

    @Override
    public void saveAll(List<E> entities) {
        try (Connection connection = connector.getConnection();
                PreparedStatement statement = connection.prepareStatement(saveQuery)) {
            for (E entity : entities) {
                insert(statement, entity);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DBRuntimeException("Save all failed", e);
        }
    }

    @Override
    public List<E> findAll() {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(findAllQuery)) {
            List<E> entities = new ArrayList<>();   
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DBRuntimeException("Find all failed", e);
        }
    }

    @Override
    public List<E> findAll(Page page) {
        int amountOnPage = page.getAmountOnPage();
        int pageNumber = page.getPageNumber();
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(findAllPagedQuery)) {
            List<E> entities = new ArrayList<>();
            preparedStatement.setInt(1, amountOnPage);
            preparedStatement.setInt(2, (pageNumber - 1) * amountOnPage);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DBRuntimeException("Find all failed", e);
        }
    }

    @Override
    public void update(E entity) {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            updateValues(preparedStatement, entity);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBRuntimeException("Update failed", e);
        }
    }

    @Override
    public void deleteById(String id) {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteByIdQuery)) {
            STRING_CONSUMER.accept(preparedStatement, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBRuntimeException("Delete by id failed", e);
        }
    }

    @Override
    public Optional<E> findById(String id) {
        return findByStringParam(id, findByIdQuery);
    }

    public void saveRelation(String firstId, String secondId, String saveRelationQuery) {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(saveRelationQuery)) {
            preparedStatement.setString(1, firstId);
            preparedStatement.setString(2, secondId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBRuntimeException("Relation insert failed", e);
        }
    }
    
    public void saveAllRelation(Map <String, List<String>> entityToEntities, String saveRelationQuery) {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(saveRelationQuery)) {
            for (Map.Entry<String, List<String>> entry : entityToEntities.entrySet()) {
                for (String entityId : entry.getValue()) {
                    preparedStatement.setString(1, entry.getKey());
                    preparedStatement.setString(2, entityId);
                    preparedStatement.addBatch();
                }
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new DBRuntimeException("Relation insert failed", e);
        }
    }

    public List<String> findRelation(String firstId, String secondId, String findRelationQuery) {
        try (Connection connection = connector.getConnection();
                PreparedStatement statement = connection.prepareStatement(findRelationQuery)) {
            statement.setString(1, firstId);
            statement.setString(2, secondId);
            List<String> ids = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ids.add(resultSet.getString(1));
                    ids.add(resultSet.getString(2));
                }
                return ids;
            }
        } catch (SQLException e) {
            throw new DBRuntimeException("Find relation failed", e);
        }
    }

    public void deleteRelation(String firstId, String secondId, String deleteRelationQuery) {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(deleteRelationQuery)) {
            preparedStatement.setString(1, firstId);
            preparedStatement.setString(2, secondId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBRuntimeException("Delete relation failed", e);
        }
    }

    protected Optional<E> findByStringParam(String param, String query) {
        return findByParam(param, query, STRING_CONSUMER);
    }

    protected List<E> findAllByIntParam(Integer param, String query) {
        return findAllByParam(param, query, INT_CONSUMER);
    }

    protected List<E> findAllByStringParam(String param, String query) {
        return findAllByParam(param, query, STRING_CONSUMER);
    }

    private <P> Optional<E> findByParam(P param, String query,
            BiConsumer<PreparedStatement, P> consumer) {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            consumer.accept(preparedStatement, param);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.ofNullable(mapResultSetToEntity(resultSet))
                        : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DBRuntimeException(e);
        }
    }

    private <P> List<E> findAllByParam(P param, String query,
            BiConsumer<PreparedStatement, P> consumer) {
        try (Connection connection = connector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            consumer.accept(preparedStatement, param);
            List<E> entities = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DBRuntimeException(e);
        }
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement preparedStatement, E entity)
            throws SQLException;

    protected abstract void updateValues(PreparedStatement preparedStatement, E entity)
            throws SQLException;
}
