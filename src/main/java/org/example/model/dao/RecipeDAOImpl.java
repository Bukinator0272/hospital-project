package org.example.model.dao;


import org.example.model.dao.constant.TableColumns;
import org.example.model.dao.executor.Executor;
import org.example.model.entity.Recipe;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAOImpl implements DAO<Recipe, Long> {

    private final Executor executor;
    private final DoctorDAOImpl doctorDAO;
    private final PatientDAOImpl patientDAO;

    public RecipeDAOImpl() {
        this.executor = new Executor();
        this.doctorDAO = new DoctorDAOImpl();
        this.patientDAO = new PatientDAOImpl();
    }

    @Override
    public void add(Recipe recipe) throws SQLException {
        executor.execUpdate("INSERT INTO RECIPE (" +
                        TableColumns.RecipeTable.DESCRIPTION + ',' +
                        TableColumns.RecipeTable.PATIENT_ID + ',' +
                        TableColumns.RecipeTable.DOCTOR_ID + ',' +
                        TableColumns.RecipeTable.CREATION_DATE + ',' +
                        TableColumns.RecipeTable.PERIOD_OF_VALIDITY + ',' +
                        TableColumns.RecipeTable.PRIORITY +
                        ") VALUES (?, ?, ?, ?, ?, ?)",
                recipe.getDescription(),
                recipe.getPatient().getId().toString(),
                recipe.getDoctor().getId().toString(),
                Date.valueOf(recipe.getCreationDate()).toString(),
                Date.valueOf(recipe.getPeriodOfValidity()).toString(),
                recipe.getPriority());
    }

    @Override
    public Recipe get(Long id) throws SQLException {
        return executor.execQuery(result -> {
                    if (!result.next())
                        return null;
                    return new Recipe(
                            id,
                            result.getString(TableColumns.RecipeTable.DESCRIPTION),
                            patientDAO.get(Long.valueOf(result.getString(TableColumns.RecipeTable.PATIENT_ID))),
                            doctorDAO.get(Long.valueOf(result.getString(TableColumns.RecipeTable.DOCTOR_ID))),
                            Date.valueOf(result.getString(TableColumns.RecipeTable.CREATION_DATE)).toLocalDate(),
                            Date.valueOf(result.getString(TableColumns.RecipeTable.PERIOD_OF_VALIDITY)).toLocalDate(),
                            result.getString(TableColumns.RecipeTable.PRIORITY));
                }, "SELECT * FROM RECIPE WHERE " + TableColumns.RecipeTable.ID + " = ?",
                id.toString());
    }

    @Override
    public void update(Recipe recipe) throws SQLException {
        executor.execUpdate("UPDATE RECIPE SET " +
                        TableColumns.RecipeTable.DESCRIPTION + " = ?," +
                        TableColumns.RecipeTable.PATIENT_ID + " = ?," +
                        TableColumns.RecipeTable.DOCTOR_ID + " = ?," +
                        TableColumns.RecipeTable.CREATION_DATE + " = ?," +
                        TableColumns.RecipeTable.PERIOD_OF_VALIDITY + " = ?," +
                        TableColumns.RecipeTable.PRIORITY + " = ? WHERE " +
                        TableColumns.RecipeTable.ID + " = ?",
                recipe.getDescription(),
                recipe.getPatient().getId().toString(),
                recipe.getDoctor().getId().toString(),
                Date.valueOf(recipe.getCreationDate()).toString(),
                Date.valueOf(recipe.getPeriodOfValidity()).toString(),
                recipe.getPriority(),
                recipe.getId().toString());
    }

    @Override
    public void delete(Recipe recipe) throws SQLException {
        executor.execUpdate("DELETE FROM RECIPE WHERE " + TableColumns.RecipeTable.ID + " = ?",
                recipe.getId().toString());
    }

    @Override
    public List<Recipe> getAll() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        return executor.execQuery(result -> {
            while (result.next()) {
                recipes.add(new Recipe(
                        result.getLong(TableColumns.RecipeTable.ID),
                        result.getString(TableColumns.RecipeTable.DESCRIPTION),
                        patientDAO.get(Long.valueOf(result.getString(TableColumns.RecipeTable.PATIENT_ID))),
                        doctorDAO.get(Long.valueOf(result.getString(TableColumns.RecipeTable.DOCTOR_ID))),
                        Date.valueOf(result.getString(TableColumns.RecipeTable.CREATION_DATE)).toLocalDate(),
                        Date.valueOf(result.getString(TableColumns.RecipeTable.PERIOD_OF_VALIDITY)).toLocalDate(),
                        result.getString(TableColumns.RecipeTable.PRIORITY)));
            }
            return recipes;
        }, "SELECT * FROM RECIPE");
    }

    public List<Recipe> getAll(String patient, String priority, String description) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        return executor.execQuery(result -> {
            while (result.next()) {
                recipes.add(new Recipe(
                        result.getLong(TableColumns.RecipeTable.ID),
                        result.getString(TableColumns.RecipeTable.DESCRIPTION),
                        patientDAO.get(Long.valueOf(result.getString(TableColumns.RecipeTable.PATIENT_ID))),
                        doctorDAO.get(Long.valueOf(result.getString(TableColumns.RecipeTable.DOCTOR_ID))),
                        Date.valueOf(result.getString(TableColumns.RecipeTable.CREATION_DATE)).toLocalDate(),
                        Date.valueOf(result.getString(TableColumns.RecipeTable.PERIOD_OF_VALIDITY)).toLocalDate(),
                        result.getString(TableColumns.RecipeTable.PRIORITY)));
            }
            return recipes;
        }, buildQuery(patient, priority, description));
    }

    private String buildQuery(String patient, String priority, String description) {
        boolean temp = false;
        String query = "SELECT * FROM RECIPE ";
        if (patient != null && !patient.isEmpty()) {
            query += "LEFT JOIN patient USING (patient_id) WHERE (" +
                    "LOWER(patient.name) like LOWER('%" + patient + "%') OR " +
                    "LOWER(patient.surname) like LOWER('%" + patient + "%') OR " +
                    "LOWER(patient.patronymic) like LOWER('%" + patient + "%')) ";
            temp = true;
        }
        if (priority != null && !priority.isEmpty()) {
            if (temp)
                query += "AND ";
            else
                query += "WHERE ";
            query += "LOWER(recipe.priority) like LOWER('%" + priority + "%') ";
            temp = true;
        }
        if (description != null && !description.isEmpty()) {
            if (temp)
                query += "AND ";
            else
                query += "WHERE ";
            query += " LOWER(recipe.description) like LOWER('%" + description + "%')";
        }
        return query;
    }

}
