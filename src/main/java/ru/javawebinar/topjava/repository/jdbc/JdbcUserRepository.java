package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final UserWithRolesMapper USER_WITH_ROLES_MAPPER = new UserWithRolesMapper();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    //    https://krishaniindrachapa.medium.com/get-results-from-join-queries-using-result-extractor-069afc4d792b
    public class UserWithRolesMapper implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> userMap = new LinkedHashMap<>();

            while (rs.next()) {
                if (userMap.containsKey(rs.getInt("id"))) {
                    User existingUser = userMap.get(rs.getInt("id"));
                    Set<Role> existingUserRoles = existingUser.getRoles();
                    existingUserRoles.add(Role.valueOf(rs.getString("role")));
                    existingUser.setRoles(existingUserRoles);
                } else {
                    User newUser = ROW_MAPPER.mapRow(rs, rs.getRow());
                    String newStringRole = rs.getString("role");
                    if (newStringRole != null) {
                        newUser.setRoles(Set.of(Role.valueOf(newStringRole)));
                    } else {
                        newUser.setRoles(Collections.EMPTY_SET);
                    }
                    userMap.put(newUser.getId(), newUser);
                }
            }
            return new ArrayList<>(userMap.values());
        }
    }

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }

        jdbcTemplate.update("DELETE FROM user_role where user_id =?", user.getId());

        if (!user.getRoles().isEmpty()) {
            Iterator<Role> iterator = user.getRoles().iterator();
            jdbcTemplate.batchUpdate("""
                       INSERT INTO user_role(user_id, role) VALUES (?,?)
                    """, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, user.getId());
                    ps.setString(2, iterator.next().toString());
                }

                @Override
                public int getBatchSize() {
                    return user.getRoles().size();
                }
            });
        }
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("""
                           SELECT *
                             FROM users
                        LEFT JOIN user_role on users.id = user_role.user_id
                            WHERE id=? """,
                USER_WITH_ROLES_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("""
                           SELECT *
                             FROM users
                        LEFT JOIN user_role on users.id = user_role.user_id
                            WHERE email=? """,
                USER_WITH_ROLES_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("""
                           SELECT *
                             FROM users
                        LEFT JOIN user_role on users.id = user_role.user_id    
                         ORDER BY name, email""",
                USER_WITH_ROLES_MAPPER);
    }
}
