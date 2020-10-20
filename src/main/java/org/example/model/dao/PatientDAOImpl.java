package org.example.model.dao;


import org.example.model.dao.constant.TableColumns;
import org.example.model.dao.executor.Executor;
import org.example.model.entity.Patient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements DAO<Patient, Long> {

    private final Executor executor;

    public PatientDAOImpl() {
        this.executor = new Executor();
    }

    @Override
    public void add(Patient patient) throws SQLException {
        executor.execUpdate("INSERT INTO PATIENT (" +
                        TableColumns.PatientTable.NAME + ',' +
                        TableColumns.PatientTable.SURNAME + ',' +
                        TableColumns.PatientTable.PATRONYMIC + ',' +
                        TableColumns.PatientTable.PHONE_NUMBER +
                        ") VALUES (?, ?, ?, ?)",
                patient.getName(),
                patient.getSurname(),
                patient.getPatronymic(),
                patient.getPhoneNumber());
    }

    @Override
    public Patient get(Long id) throws SQLException {
        return executor.execQuery(result -> {
                    if (!result.next())
                        return null;
                    return new Patient(
                            id,
                            result.getString(TableColumns.PatientTable.NAME),
                            result.getString(TableColumns.PatientTable.SURNAME),
                            result.getString(TableColumns.PatientTable.PATRONYMIC),
                            result.getString(TableColumns.PatientTable.PHONE_NUMBER));
                }, "SELECT * FROM PATIENT WHERE " + TableColumns.PatientTable.ID + " = ?",
                id.toString());
    }

    @Override
    public void update(Patient patient) throws SQLException {
        executor.execUpdate("UPDATE PATIENT SET " +
                        TableColumns.PatientTable.NAME + " = ?," +
                        TableColumns.PatientTable.SURNAME + " = ?," +
                        TableColumns.PatientTable.PATRONYMIC + " = ?," +
                        TableColumns.PatientTable.PHONE_NUMBER + " = ? WHERE " +
                        TableColumns.PatientTable.ID + " = ?",
                patient.getName(),
                patient.getSurname(),
                patient.getPatronymic(),
                patient.getPhoneNumber(),
                patient.getId().toString());
    }

    @Override
    public void delete(Patient patient) throws SQLException {
        executor.execUpdate("DELETE FROM PATIENT WHERE " + TableColumns.PatientTable.ID + " = ?",
                patient.getId().toString());
    }

    @Override
    public List<Patient> getAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        return executor.execQuery(result -> {
            while (result.next()) {
                patients.add(new Patient(
                        result.getLong(TableColumns.PatientTable.ID),
                        result.getString(TableColumns.PatientTable.NAME),
                        result.getString(TableColumns.PatientTable.SURNAME),
                        result.getString(TableColumns.PatientTable.PATRONYMIC),
                        result.getString(TableColumns.PatientTable.PHONE_NUMBER)));
            }
            return patients;
        }, "SELECT * FROM PATIENT");
    }

}
