package org.example.model.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<Entity, Key> {

    void add(Entity entity) throws SQLException;

    Entity get(Key key) throws SQLException;

    void update(Entity entity) throws SQLException;

    void delete(Entity entity) throws SQLException;

    List<Entity> getAll() throws SQLException;

}
