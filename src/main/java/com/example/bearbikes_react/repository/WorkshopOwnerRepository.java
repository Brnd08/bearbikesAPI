package com.example.bearbikes_react.repository;

import com.example.bearbikes_react.model.Cyclist;
import com.example.bearbikes_react.model.WorkshopOwner;
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
public class WorkshopOwnerRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    WorkshopOwnerRepository(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Makes a query to the database to count the registered workshop owners
     * @return the number of cyclists in the database
     */
    public int countWorkshopOwners(){
        String countUsersQuery = "SELECT COUNT(*) FROM dueñotaller";
        return jdbcTemplate.queryForObject(countUsersQuery, Integer.class);
    }

    /**
     * Makes a call to a stored procedure in the database in order to add a new row in the dueñoTaller table
     * using a given WorkshopOwner object
     * @param newWorkshopOwner WorkshopOwner object to insert
     * @return the id of the new WorkshopOwner in the database, or -1 if an exception happened
     */
    public int addWorkshopOwner(WorkshopOwner newWorkshopOwner){
        SimpleJdbcCall addUserProcedureCall
                = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("insertar_dueño_taller")
                .declareParameters(
                        new SqlParameter("emailUsuario", Types.VARCHAR),
                        new SqlParameter("passwordUsuario", Types.VARCHAR),
                        new SqlParameter("nombreDueño", Types.VARCHAR),
                        new SqlParameter("apellidoPat", Types.VARCHAR),
                        new SqlParameter("apellidoMat", Types.VARCHAR),
                        new SqlParameter("numeroCelular", Types.VARCHAR),
                        new SqlParameter("RFCFisica", Types.VARCHAR),
                        new SqlOutParameter("idUsuarioInsertado", Types.INTEGER)
                );
        Map<String, Object> result = addUserProcedureCall.execute(
                newWorkshopOwner.getEmail(),
                newWorkshopOwner.getPassword(),
                newWorkshopOwner.getNombre(),
                newWorkshopOwner.getApellidoPat(),
                newWorkshopOwner.getApellidoMat(),
                newWorkshopOwner.getNumerocelular(),
                newWorkshopOwner.getRfcFisica()
        );
        int insertedWorkshopOwnerId = (int) result.getOrDefault("idUsuarioInsertado", -1);

        newWorkshopOwner.setId(insertedWorkshopOwnerId);
        return insertedWorkshopOwnerId;
    }
}
