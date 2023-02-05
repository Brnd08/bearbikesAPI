package com.example.bearbikes_react.repository;

import com.example.bearbikes_react.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

@Repository
public class AdminsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    AdminsRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
        Ciclist realted Methods
     */

    /**
     * Makes a query to the database to count the registered admins
     * @return the number of admins in the database
     */
    public int countAdmins(){
        String countUsersQuery = "SELECT COUNT(*) FROM administradores";
        return jdbcTemplate.queryForObject(countUsersQuery, Integer.class);
    }

    /**
     * Makes a call to a stored procedure in the database in order to add a new row in the administradores table
     * using a given Ciclist object
     * @param newAdmin Ciclist object to insert
     * @return the id of the new Ciclist in the database, or -1 if an exception happened
     */
    public int addAdmin(Admin newAdmin){
        SimpleJdbcCall addUserProcedureCall
                = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("insertar_admin")
                .declareParameters(
                        new SqlParameter("emailUsuario", Types.VARCHAR),
                        new SqlParameter("passwordUsuario", Types.VARCHAR),
                        new SqlParameter("nombreAdmin", Types.VARCHAR),
                        new SqlOutParameter("idUsuarioInsertado", Types.INTEGER)
                );
        Map<String, Object> result = addUserProcedureCall.execute(
                newAdmin.getEmail(),
                newAdmin.getPassword(),
                newAdmin.getNombre()
        );

        int insertedAdminId = (int) result.getOrDefault("idUsuarioInsertado", -1);
        newAdmin.setId(insertedAdminId);
        return insertedAdminId;
    }
}
