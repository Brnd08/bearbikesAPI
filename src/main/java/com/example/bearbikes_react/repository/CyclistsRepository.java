package com.example.bearbikes_react.repository;

import com.example.bearbikes_react.model.Cyclist;
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
public class CyclistsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    CyclistsRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
        Ciclist realted Methods
     */

    /**
     * Makes a query to the database to count the registered cyclists
     * @return the number of cyclists in the database
     */
    public int countCyclists(){
        String countUsersQuery = "SELECT COUNT(*) FROM ciclistas";
        return jdbcTemplate.queryForObject(countUsersQuery, Integer.class);
    }

    /**
     * Makes a call to a stored procedure in the database in order to add a new row in the ciclistas table
     * using a given Ciclist object
     * @param newCyclist Ciclist object to insert
     * @return the id of the new Ciclist in the database, or -1 if an exception happened
     */
    public int addCyclist(Cyclist newCyclist){
        SimpleJdbcCall addUserProcedureCall
                = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("insertar_ciclista")
                .declareParameters(
                        new SqlParameter("emailUsuario", Types.VARCHAR),
                        new SqlParameter("passwordUsuario", Types.VARCHAR),
                        new SqlParameter("nombreCiclista", Types.VARCHAR),
                        new SqlParameter("apellidoPat", Types.VARCHAR),
                        new SqlParameter("apellidoMat", Types.VARCHAR),
                        new SqlParameter("numeroCelular", Types.VARCHAR),
                        new SqlParameter("tokenPersonalCiclista", Types.VARCHAR),
                        new SqlOutParameter("idUsuarioInsertado", Types.INTEGER)
                );
        Map<String, Object> result = addUserProcedureCall.execute(
                newCyclist.getEmail(),
                newCyclist.getPassword(),
                newCyclist.getNombre(),
                newCyclist.getApellidoPat(),
                newCyclist.getApellidoMat(),
                newCyclist.getNumerocelular(),
                newCyclist.getTokenPersonal()
        );
        int insertedCiclistId = (int) result.getOrDefault("idUsuarioInsertado", -1);
        newCyclist.setId(insertedCiclistId);
        return insertedCiclistId;
    }
}
